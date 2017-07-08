#Useful Ansible Commands

**Ping All Servers**
``` 
ansible all -m ping -i hosts
```

**Ping a Server**
``` 
ansible pi-load-balancer -m ping -i hosts
```

**Ping a Group**
``` 
ansible servers -m ping -i hosts
```
