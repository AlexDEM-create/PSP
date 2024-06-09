**How to set up the platform**

Follow this guide to install kubernetes https://hbayraktar.medium.com/how-to-install-kubernetes-cluster-on-ubuntu-22-04-step-by-step-guide-7dbf7e8f5f99

Follow this guide to install metallb https://metallb.universe.tf/installation/

Follow this guild to install helm https://helm.sh/docs/intro/install/

Run:

`cd tech

./deploy.sh

sudo kubectl apply -f flacko-pay/pool-1.yaml

sudo kubectl apply -f flacko-pay/l2-advertisement.yaml

sudo helm pull oci://ghcr.io/nginxinc/charts/nginx-ingress --untar --version 1.2.2

cd nginx-ingress

sudo kubectl apply -f crds

sudo helm install nginx-ingress oci://ghcr.io/nginxinc/charts/nginx-ingress --version 1.2.2`

