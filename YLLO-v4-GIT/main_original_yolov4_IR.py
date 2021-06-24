#!/usr/bin/python
# usage python fimename.py --input /path_to_videos/D3_1.mp4 --yolo yolo-coco

import requests
import base64
import json

# import the necessary packages
from sort import *
import numpy as np
import argparse
import imutils
import time
import cv2
import os
import glob
from operator import itemgetter
from imutils.video import FPS
from imutils.video import VideoStream

#To run OPEL-ALPR requires key and the API details after sign up
SECRET_KEY = 'sk_916470fefeec02120f2dbfb5'
url = 'https://api.openalpr.com/v2/recognize_bytes?recognize_vehicle=1&country=us&secret_key=%s' % (SECRET_KEY)
global labels
labels = []
global boxesd
boxesd = []
global confidences
confidences = []
global classIDs
classIDs = []
global confidences1
confidences1 = []
global classIDs1
classIDs1 = []
global a
a = []
global sort1
sort1 = []
global track_ids
track_ids = []
global array1
array1 = []
global start_time
start_time =0
global end_time
end_time =0
TGREEN = '\033[32m'
TWHITE = '\033[37m'
TYELLOW = '\033[33m'
TBLUE = '\033[34m'
TPURPLE = '\033[35m'
TCYAN = '\033[36m'
TRED = '\033[31m'

#file = open('annotation.txt', 'w+')
#file.write('FRAME_NUMBER' + ',' + 'ID1' + ','
           #'ID2' +',' + 'ID3' + ',' + 'ID4' + ',' + 'ID5' + ',' + 'ID6' + ',' + 'ID7' + ','  + 'ID8' + ','+ 'ID9' + ',' +  'ID10'  + '\n')
#file.write('\n')

#Call SORT tracking algorithm
tracker = Sort()
memory = {}
line = [(43, 543), (550, 655)]
counter = 0

# construct the argument parse and parse the arguments
ap = argparse.ArgumentParser()
ap.add_argument("-i", "--input", required=True,
                help="path to input video")
ap.add_argument("-y", "--yolo", required=True,
                help="base path to YOLO directory")
ap.add_argument("-c", "--confidence", type=float, default=0.25,
                help="minimum probability to filter weak detections")
ap.add_argument("-t", "--threshold", type=float, default=0.3,
                help="threshold when applyong non-maxima suppression")
args = vars(ap.parse_args())



# load the COCO class labels our YOLO model was trained on
labelsPath = os.path.sep.join([args["yolo"], "coco.names"])
LABELS = open(labelsPath).read().strip().split("\n")

# initialize a list of colors to represent each possible class label
np.random.seed(42)
COLORS = np.random.randint(0, 255, size=(200, 3),
                           dtype="uint8")

# derive the paths to the YOLO weights and model configuration
weightsPath = os.path.sep.join([args["yolo"], "frozen_darknet_yolov4_model.bin"])
configPath = os.path.sep.join([args["yolo"], "frozen_darknet_yolov4_model.xml"])

#Use these when running w/o OPENVINO/NCS2
#weightsPath = os.path.sep.join([args["yolo"], "yolov4.weights"])
#configPath = os.path.sep.join([args["yolo"], "yolov4.cfg"])
start_time = time.time()

# load our YOLO object detector trained on COCO dataset (80 classes)
# and determine only the *output* layer names that we need from YOLO
print(TGREEN + "[INFO] loading YOLO from disk...")
# Intializing colors to represent each label uniquely
colors = np.random.randint(0, 255, size=(len(LABELS), 3), dtype='uint8')

# Load the weights and configutation to form the pretrained YOLOv1/2/3 model
#net = cv2.dnn.readNetFromDarknet(configPath,weightsPath)


#For yolov4 use this and disable for pther yolo versions
model = cv2.dnn.readNet(configPath,weightsPath)
net = cv2.dnn_DetectionModel(model)
net.setInputParams(size=(416,416), scale = 1/255)

# specify the target device as the Myriad processor on the NCS2
net.setPreferableTarget(cv2.dnn.DNN_TARGET_MYRIAD) #added when using intelNCS2

# Get the output layer names of the model - disable for openvino IR
#ln = net.getLayerNames()
#ln = [ln[i[0] - 1] for i in net.getUnconnectedOutLayers()]

#print('ln value ==> ' + str(ln))
# initialize the video stream, pointer to output video file, and
# frame dimensions


vs = cv2.VideoCapture(args["input"])
#vs = cv2.VideoCapture(0)
time.sleep(2.0)
fps = FPS().start()
elseif_counter=0
elapsedTime = 0
countv = 0
try:
	prop = cv2.cv.CV_CAP_PROP_FRAME_COUNT if imutils.is_cv2() \
		else cv2.CAP_PROP_FRAME_COUNT
	total = int(vs.get(prop))
	print("[INFO] {} total frames in video".format(total))

# an error occurred while trying to determine the total
# number of frames in the video file
except:
	print("[INFO] could not determine # of frames in video")
	print("[INFO] no approx. completion time can be provided")
	total = -1
print("[INFO] starting video stream...")

while True:
    ret, frame = vs.read()
    
    if ret == True :
        (H, W) = frame.shape[:2]
        #blob = cv2.dnn.blobFromImage(frame, 1 / 255.0, (288, 288), swapRB=True, crop=False)
        blob = cv2.dnn.blobFromImage(frame, size=(672, 384))
        net.setInput(blob)
        #Use this for normal yolo models - disable with openvino yolov4
        #layerOutputs = net.forward(ln)
        #Use this with openvino yolov4
        layerOutputs = net.forward()
        frame_counter= False
        sort1 = []
        #file.write('\n')
        t0 = time.time()
        for output in layerOutputs:
            sorted_list = sorted(output, key=itemgetter(4), reverse=True)
            array1.append(sorted_list)
        
        for item in sorted_list:
            scores_d = item[5:]
            
            classIDs1 = np.argmax(scores_d)
            
            if classIDs1 > 0:
                confidence1 = scores_d[classIDs1]
                sort1.append(classIDs1)
                sort1.sort()
        #print("The number of bounding boxes found before detection is")
        #print(len(sort1))
        #print("class IDs are")
        #print(sort1)
            
            
        if countv == 0:
            #frame_counter = True
            boxesd = []
            confidences = []
            classIDs = []
            for item in sorted_list:
                scores_d = item[5:]
                classIDs1 = np.argmax(scores_d)
                confidence1 = scores_d[classIDs1]
                if confidence1 > args["confidence"]:
                            # scale the bounding box coordinates back relative to
                            # the size of the image, keeping in mind that YOLO
                            # actually returns the center (x, y)-coordinates of
                            # the bounding box followed by the boxes' width and
                            # height
                    box = item[0:4] * np.array([W, H, W, H])
                    (centerX, centerY, width, height) = box.astype("int")

                            # use the center (x, y)-coordinates to derive the top
                            # and and left corner of the bounding box
                    x = int(centerX - (width / 2))
                    y = int(centerY - (height / 2))

                            # update our list of bounding box coordinates,
                            # confidences, and class IDs
                    boxesd.append([x, y, int(width), int(height)])
                    confidences.append(float(confidence1))
                    classIDs.append(classIDs1)
                    classIDs.sort()
            #print('The number of Bounding Boxes found after detection inside IF is', len(boxesd))
            #print(classIDs)
            #print(confidences)
        
        elif (len(sort1) != len(boxesd)) and (np.array_equal(classIDs, classIDs1)!= True):
            elseif_counter +=1 
            frame_counter = True
            boxesd = []
            confidences = []
            classIDs = []
            for item in sorted_list:
                scores_d = item[5:]
                classIDs1 = np.argmax(scores_d)
                confidence1 = scores_d[classIDs1]
                if confidence1 > args["confidence"]:
                            # scale the bounding box coordinates back relative to
                            # the size of the image, keeping in mind that YOLO
                            # actually returns the center (x, y)-coordinates of
                            # the bounding box followed by the boxes' width and
                            # height
                    box = item[0:4] * np.array([W, H, W, H])
                    (centerX, centerY, width, height) = box.astype("int")

                            # use the center (x, y)-coordinates to derive the top
                            # and and left corner of the bounding box
                    x = int(centerX - (width / 2))
                    y = int(centerY - (height / 2))

                            # update our list of bounding box coordinates,
                            # confidences, and class IDs
                    boxesd.append([x, y, int(width), int(height)])
                    confidences.append(float(confidence1))
                    classIDs.append(classIDs1)
                    classIDs.sort()
                    #labels.append(LABELS(classIDs))
            
        
        # apply non-maxima suppression to suppress weak, overlapping
        # bounding boxes
        idxs = cv2.dnn.NMSBoxes(boxesd, confidences,
                                args["confidence"], args["threshold"])
        #print(len(idxs))
        dets = []
        if len(idxs) > 0:
            # loop over the indexes we are keeping
            for i in idxs.flatten():
                (x, y) = (boxesd[i][0], boxesd[i][1])
                (w, h) = (boxesd[i][2], boxesd[i][3])
                dets.append([x, y, x+w, y+h, confidences[i]])
            
            #print(TPURPLE +"IOU of BB")
            #print(confidences)
            np.set_printoptions(formatter={'float': lambda x: "{0:0.3f}".format(x)})
            dets = np.asarray(dets)
            tracks = tracker.update(dets)
        
        boxes = []
        indexIDs = []
        c = []
        previous = memory.copy()
        memory = {}

        for track in tracks:
            boxes.append([track[0], track[1], track[2], track[3]])
            indexIDs.append(int(track[4]))
            memory[indexIDs[-1]] = boxes[-1]
        
        if len(boxes) > 0:
            i = int(0)
            for box in boxes:
                    # extract the bounding box coordinates
                (x, y) = (int(box[0]), int(box[1]))
                (w, h) = (int(box[2]), int(box[3]))

                # draw a bounding box rectangle and label on the image
                # color = [int(c) for c in COLORS[classIDs[i]]]
                # cv2.rectangle(frame, (x, y), (x + w, y + h), color, 2)

                color = [int(c) for c in COLORS[indexIDs[i] % len(COLORS)]]
                cv2.rectangle(frame, (x, y), (w, h), color, 2)

                if indexIDs[i] in previous:
                    previous_box = previous[indexIDs[i]]
                    (x2, y2) = (int(previous_box[0]), int(previous_box[1]))
                    (w2, h2) = (int(previous_box[2]), int(previous_box[3]))
                    p0 = (int(x + (w-x)/2), int(y + (h-y)/2))
                    p1 = (int(x2 + (w2-x2)/2), int(y2 + (h2-y2)/2))
                    cv2.line(frame, p0, p1, color, 3)

                    # if intersect(p0, p1, line[0], line[1]):
                    #counter += 1

                text = "{}".format(indexIDs[i])
                cv2.putText(frame, text, (x, y - 5),
                            cv2.FONT_HERSHEY_SIMPLEX, 0.5, color, 2)
                #cv2.putText(frame, text1, (x, y - 5), cv2.FONT_HERSHEY_SIMPLEX, 0.5, color, 2)
                i += 1
        t1 = time.time() - t0
        

        #Creates Base64 encoded bytes of the video frame
        img_base64 = base64.b64encode(frame)
        #Creates a POST request at ED to EC to send the frames
        r = requests.post(url, data=img_base64)
        print(json.dumps(r.json(), indent=1))
        
        #use the below 4 lines to display the video on the screen
        #cv2.imshow("Frame", frame)
        #key = cv2.waitKey(1) & 0xFF
        #if key == ord("q"):
            #break

        cv2.imwrite("/Users/pavana/Desktop/YLLO/frames/frame-{}.png".format(countv), frame)
        print('Processing %d frame' % countv)
        cv2.imwrite("frames/frame-{}.png".format(countv), frame)
        print(frame_counter)
        if frame_counter == True or countv == 0:
            #The frames sent to EC are saved in sampling folder
            cv2.imwrite("/Users/pavana/Desktop/YLLO/sampling/frame-{}.png".format(countv), frame)
            requests.get("http://localhost:8080/edge-device/frames?frameId=frame-{}.png".format(countv))

        elif frame_counter == True:
            #The frames saved at EDs
            cv2.imwrite("frames/frame-{}.png".format(countv), frame)        
        
    
        #ctr += 1
        countv += 1
    else :
        break 


print("Total Time taken is --- %s seconds ---" % (time.time() - start_time))
print("number of time nueral network layer invoked is")
print(elseif_counter)
cv2.destroyAllWindows()

