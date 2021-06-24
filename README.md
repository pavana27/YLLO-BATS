# YLLO-BATS

YLLO is lightweight object detection technique to run on Edge Devices and BATS exploits the overlaps between the views of multiple cameras and dynamically adapts to the available transmission bandwidth to the Edge Controller.

YLLO uses YOLOv4 and does Real-time Object Detection on Edge Devices using Intel Movidius Neural Compute Stick2 using OpenVino-toolkit. 

To get started with NCS, please refer [[1]](#1),
More description on converting YOLOv4 pre trained weights into OPENVINO format can be found in [[2]](#2).

Pretrained YOLOv4 weights can be found [[3]](#3). Before running YLLO, Please place the downloaded weights inside **yolo-coco** folder under src. 



## References
<a id="1">[1]</a>
https://www.pyimagesearch.com/2018/02/12/getting-started-with-the-intel-movidius-neural-compute-stick/ 

<a id="2">[2]</a>
https://towardsdatascience.com/real-time-object-detection-on-cpu-9f77d32deeaf

<a id="3">[3]</a>
https://zenodo.org/record/3829035#.YNUCcZNKj0p
