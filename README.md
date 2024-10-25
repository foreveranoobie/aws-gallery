# aws-gallery

1. Check if cloudwatch agent is running systemctl status amazon-cloudwatch-agent
2. If user-data script isn't executed, run:
sudo  rm -rf /var/lib/cloud/*
sudo cloud-init init
sudo cloud-init modules -m final
