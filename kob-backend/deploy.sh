#!/bin/bash
# KOB 一键部署
# 用法:
#   ./deploy.sh          - 部署前端 + 后端
#   ./deploy.sh front    - 仅部署前端
#   ./deploy.sh back     - 仅部署后端

set -e

SERVER="hilyc"
REMOTE_JAR_DIR="/home/lyc/jars/kob"
CONTAINER="kob-backend"
NGINX="global_nginx"

deploy_frontend() {
    echo "[前端] Building..."
    cd ../kob-web && pnpm build
    echo "[前端] Uploading..."
    cd dist
    ssh $SERVER "rm -rf /home/lyc/kob-web/*; mkdir -p /home/lyc/kob-web"
    scp -r * $SERVER:/home/lyc/kob-web/
    ssh $SERVER "docker exec $NGINX rm -rf /var/www/kob/*"
    ssh $SERVER "docker cp /home/lyc/kob-web/. $NGINX:/var/www/kob/"
    cd ../..
    echo "[前端] Done!"
}

deploy_backend() {
    echo "[后端] Building..."
    mvn clean package -DskipTests
    echo "[后端] Uploading jars..."
    scp kob-backend/target/backend-0.0.1-SNAPSHOT.jar $SERVER:$REMOTE_JAR_DIR/backend-0.0.1-SNAPSHOT.jar
    scp matching-system/target/matching-system-0.0.1-SNAPSHOT.jar $SERVER:$REMOTE_JAR_DIR/matching-system-0.0.1-SNAPSHOT.jar
    scp botrunning-system/target/botrunning-system-0.0.1-SNAPSHOT.jar $SERVER:$REMOTE_JAR_DIR/botrunning-system-0.0.1-SNAPSHOT.jar
    echo "[后端] Restarting container..."
    ssh $SERVER "docker restart $CONTAINER"
    echo "[后端] Done!"
}

cd "$(dirname "$0")"

case "${1:-all}" in
    front|frontend|f)
        deploy_frontend
        ;;
    back|backend|b)
        deploy_backend
        ;;
    all|"")
        deploy_frontend
        deploy_backend
        ;;
    *)
        echo "用法: ./deploy.sh [front|back|all]"
        exit 1
        ;;
esac

echo "Deploy complete!"
