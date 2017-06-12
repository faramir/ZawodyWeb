tar -zvcf "logs_`date +'%y%m%d_%H%M%S'`.tgz" *.log *.log.* *.pid *.out submits/[0-9]* externals/*
rm -vr *.log *.log.* *.pid *.out submits/[0-9]* externals/*
