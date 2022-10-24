scp dist/assets/*.js springboot:kob/acapp/
scp dist/assets/*.css springboot:kob/acapp/

ssh springboot 'cd kob/acapp && ./rename.sh'
