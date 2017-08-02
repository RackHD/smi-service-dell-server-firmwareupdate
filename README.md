Service to compare and apply firmware updates for a Dell server using a repository catalog.

[![Quality Gate](http://100.68.126.201:9000/api/badges/gate?key=org.sonarqube:service-server-firmwareupdate)](http://100.68.126.201:9000/dashboard/index/org.sonarqube:service-server-firmwareupdate)

### Purpose
For use with Dell 11th Generation and newer servers. 
- Download the catalog from ftp.dell.com to a local share
- Determine updates from a Dell Repository Manager catalog that can be applied to a Dell server
- Create a custom catalog
- Schedule updates from a Dell Repository Manager catalog to a dell server


### Overview
The docker container Exposes a REST API that provides methods to orchestrate the comparison and upgrade Dell 11th Generation and newer servers using a Dell Repository Catalog.   The catalog.xml file in the repository contains information about the Update Bundles and their "Dell Update Packages" (DUPs) available in the repository.

By default the dell-server-firmwareupdate docker-container will use the catalog available on ftp.dell.com, unless a custom catalog is specified in the request.

To update firmware, a NFS or CIFS folder with write permission must be mounted on the host, and the local location provided as part of the Docker run command. This share must be accessible by the IDRACs of the Dell servers that you wish to update.  When a firmware update is initiated DUPs are stored in a folder this share, and downloaded from there by the IDRAC.

### Prerequisites
- Docker running on Linux
- A NFS or CIFS share running locally or mounted locally on the docker host. 
- The IDRAC must have read access to the NFS or CIFS host
- The IDRAC must be accessible from the Docker Container
- Access to ftp.dell.com, or a repository created by the Dell Repository Manager placed on a network share.

---

### How to Launch
1. Mount the NFS or CIFS share on the Host Linux based OS that is Running Docker (example /mnt/myShare/myFolder)
2. Start the Docker Container
~~~
sudo docker run -p 0.0.0.0:46010:46010 --name dell-server-firmwareupdate -v /mnt/myShare/myFolder:/fwRepo -d rackhd/dell-server-firmwareupdate:1.0.0
~~~
3.  Verify the shared directory exists and you can see any files you have on the share from within your docker container.
~~~
sudo docker exec -it dell-server-firmwareupdate /bin/bash
ls fwRepo
~~~
4.  Verify you can access the Swagger UI at http://<<your ip>>:46010/swagger-ui.html
5.  Call the Version API (to make sure the API is working).
~~~
http://<<your ip>>:46010/api/1.0/server/firmware/version
~~~
___

### How to Use

#### API Definitions
A swagger UI is provided bythe microservice at: 
http://<<your ip>>:46010/swagger-ui.html

#### Example Firmware Update API Flow (Without Using RackHD)
1. Put a Dell catalog file on the mounted share
Option 1 - Download the catalog from ftp.dell.com
~~~
http://<<your ip>>:46010/api/1.0/server/firmware/downloader?fileName=Catalog.xml.gz&fileUrl=ftp.dell.com/catalog&targetLocation=/fwRepo
~~~
Option 2 - Alternatively, you can use Dell Repository Manager to create a custom repository for your system(s) and place the files, to include the catalog file, on your network share.
2. Determine the DUPs in the catalog that are available to be applied to a specific Dell server.
~~~
http://<<your ip>>:46010/api/1.0/server/firmware/comparer
{"address":"<target idrac>","catalogPath":"nfs/Catalog.xml","type":"wsman",updateableComponentInventory:"string"} 
~~~
The curl command above will return a list of applicable updates (DUPS) for the systems specified. 
3. Create a custom repository for your server that contain only the DUPs you want to apply.  
-- If using ftp.dell.com for your catalog, this step will download DUPs from the internet into the new repository.  
-- If using a Dell Repository Manager catalog, it will copy the DUPs from the catalog's repository into the new repository.

Ideally, a consuming UI will read the results from the previous step, allow you to pick the updates, and build the payload for this method. Alternatively you can manually construct a CURL command using the data returned from the previous step, removing the components you do not wish to apply.  You can also apply all of the results from the previous method by using it directly as your payload.

Example payload
~~~
http://<<your ip>>:46010/api/1.0/server/firmware/comparer/custom
       {
	"catalogFilePath":"nfs/Catalog.xml",
    "targetFilePath":"nfs/repo",
 "updates": [
      {
        "version": "2.23",
        "path": "FOLDER02909018M/6/Firmware_635G9_WN32_2.23_A00-00.EXE",
        "name": "13G SEP Firmware",
        "criticality": "Recommended",
        "uniqueIdentifier": "635G9LWXP",
        "updateAction": "EQUAL",
        "targetIdentifier": "101434",
        "packageInformationPath": "FOLDER02909018M/6/Firmware_635G9_WN32_2.23_A00-00.EXE",
        "sourceName": "DCIM:INSTALLED#314_C_RAID.Backplane.Firmware.1",
        "category": "Firmware"
      }
    ]
}
~~~ 
4. Initiate a firmware update job for the server.
~~~
http://<<your ip>>:46010/v1/server/firmware/updater
{
	"serverAddress": "<<target_server_idrac_ip>>",
	"shareAddress" : "<<IP of the NFS server>>",
	"shareName": "/nfs/repo",
	"catalogFileName" : "Catalog.xml",
	"shareType" : "0",
	"shareUserName" : "user1",
	"sharePassword" : "user1",
	"applyUpdate" : "1",
	"Reboot" : "YES"  
}
~~~

#### Example Firmware Update Using RackHD
The <<todo: add taskgraph name>> taskgraph allows you to apply all of the applicable firmware for a bundle in a repository to a given server. 

#### Example Firmware Update API Flow with callback monitoring
1. Set up a way to listen for http data
a. On a linux computer that you would like to use as the callback target, start netcat. Netcat, when started with the options in the example below, it will echo any requests it receives on port 5000 to the terminal.
~~~
netcat -kl 5000
~~~
b. Test it out by making a http request to port 5000  http://<<your callback target ip>:5000/foo/bar
2. Make the following CURL calls to these endpoints:
TODO:

---

### Support
Slack Channel: codecommunity.slack.com