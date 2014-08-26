#!/bin/bash

sudo echo "Installing chef solo \n" >> /tmp/salsa.artifact.log
sudo useradd guest >> /tmp/salsa.artifact.log
sudo aptitude install -y emacs-nox >> /tmp/salsa.artifact.log
sudo aptitude update -y >> /tmp/salsa.artifact.log
sudo aptitude safe-upgrade -y >> /tmp/salsa.artifact.log
sudo aptitude install -y ruby ruby1.8-dev build-essential wget libruby1.8 rubygems >> /tmp/salsa.artifact.log
sudo gem update --no-rdoc --no-ri >> /tmp/salsa.artifact.log
sudo gem install -y ohai chef --no-rdoc --no-ri >> /tmp/salsa.artifact.log

sudo echo "Finished installing chef solo \n" >> /tmp/salsa.artifact.log
