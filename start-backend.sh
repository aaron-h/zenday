#!/bin/bash

echo "=========================================="
echo "  禅修功课管理系统 - 后端启动脚本"
echo "=========================================="
echo ""

# 检查 Java 版本
if ! command -v java &> /dev/null; then
    echo "❌ 错误: 未找到 Java，请先安装 Java 17 或更高版本"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ 错误: Java 版本过低，需要 Java 17 或更高版本"
    exit 1
fi

echo "✅ Java 版本检查通过: $(java -version 2>&1 | head -n 1)"
echo ""

# 进入后端目录
cd backend || exit

# 检查是否已打包
if [ ! -f "target/zenday-backend-1.0.0.jar" ]; then
    echo "📦 未找到打包文件，开始打包..."
    mvn clean package -DskipTests

    if [ $? -ne 0 ]; then
        echo "❌ 打包失败，请检查错误信息"
        exit 1
    fi
    echo "✅ 打包成功"
    echo ""
fi

# 启动后端服务
echo "🚀 启动后端服务..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
java -jar target/zenday-backend-1.0.0.jar

# 如果需要后台运行，使用以下命令：
# nohup java -jar target/zenday-backend-1.0.0.jar > ../logs/backend.log 2>&1 &
# echo "✅ 后端服务已在后台启动，日志文件: logs/backend.log"
