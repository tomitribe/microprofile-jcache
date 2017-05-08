Flash Hypriot Images
 - https://github.com/hypriot/flash
 	- flash --hostname pi-client-01 https://github.com/hypriot/image-builder-rpi/releases/download/v1.4.0/hypriotos-rpi-v1.4.0.img.zip
 	- flash --hostname pi-client-02 hypriotos-rpi-v1.4.0.img
 	- flash --hostname pi-client-03 hypriotos-rpi-v1.4.0.img
    - flash --hostname pi-client-04 hypriotos-rpi-v1.4.0.img
    - flash --hostname pi-grom-server-01 hypriotos-rpi-v1.4.0.img
    - flash --hostname pi-grom-server-02 hypriotos-rpi-v1.4.0.img
    - flash --hostname pi-thrall-server-01 hypriotos-rpi-v1.4.0.img
    - flash --hostname pi-thrall-server-02 hypriotos-rpi-v1.4.0.img
    - flash --hostname pi-thrall-load-balancer hypriotos-rpi-v1.4.0.img
  	- flash --hostname pi-grom-load-balancer hypriotos-rpi-v1.4.0.img
  	- flash --hostname pi-load-balancer hypriotos-rpi-v1.4.0.img
  	- flash --hostname pi-thrall-database hypriotos-rpi-v1.4.0.img
    - flash --hostname pi-grom-database hypriotos-rpi-v1.4.0.img

Router:
 - Login into the router with ubnt / ubnt.
 - Wizards / WAN + 2LAN2 to set up DHCP Server (Used 10.99.99.1 / 255.255.255.0)
 - Services / LAN / View Details / Leases / Map Static IP (Map MAC Address to static IPs)
 - Set Configuration dhcp-server / hostfile-update to enable. (This will allow to reach the boxes by hostname)
 - Set manual hostname for local docker repo (own box)
 	- configure
 	- set system static-host-mapping host-name docker-repo inet 10.99.99.11
 	- commit
	- save
	- exit

Ansible:
 - ansible -i hosts all -m ping
 - ansible -i hosts servers -m ping
 - ansible -i hosts clients -m ping
 - ansible -i hosts load-balancers -m ping
 - ansible-playbook load-balancers.play -i hosts -f 2
 - ansible-playbook install-tomee.play -i hosts -f 8
 - ansible-playbook servers.play -i ../hosts -f 4
 - ansible-playbook clients.play -i ../hosts -f 3
 - ansible-playbook load-balancers.play -i ../hosts -f 3
 - ansible-playbook install-mysql.play -i ../hosts -f 2
 - ansible -i hosts servers,clients -m docker_image -a "name=radcortez/rpi-tomee:8-jre-7.0.3-plus state=absent force=yes"
 - ansible -i hosts clients -m docker_container -a "name=client state=stopped"
 - ansible -i hosts servers -m docker_container -a "name=server state=stopped"

Docker
 - docker exec -it load-balancer bash
 - docker build -t radcortez/rpi-tomee:8-jre-7.0.3-webprofile .
 - docker run -t --entrypoint bash server

Docker Repo
 - https://docs.docker.com/registry/deploying/
 - openssl req -newkey rsa:4096 -nodes -sha256 -keyout docker-repo.key -x509 -days 365 -out docker-repo.crt
 - docker run -d -p 5000:5000 --restart=always --name docker-repo -v `pwd`/certs:/certs -e REGISTRY_HTTP_TLS_CERTIFICATE=/certs/docker-repo.crt -e REGISTRY_HTTP_TLS_KEY=/certs/docker-repo.key registry:2
 - Need to add self signed certificates. No easy way to do it on mac. Easy way is to just add the repo in Docker Daemon Preferences.

 - docker push docker-repo:5000/radcortez/rpi-tomee:8-jre-7.0.3-webprofile
 - docker pull docker-repo:5000/radcortez/rpi-tomee:8-jre-7.0.3-webprofile
