#!/bin/bash

echo "=========================================="
echo "  禅修功课管理系统 - 前端启动脚本"
echo "=========================================="
echo ""

# 检查 Node.js 版本
if ! command -v node &> /dev/null; then
    echo "❌ 错误: 未找到 Node.js，请先安装 Node.js 16 或更高版本"
    exit 1
fi

NODE_VERSION=$(node -v | cut -d'v' -f2 | cut -d'.' -f1)
if [ "$NODE_VERSION" -lt 16 ]; then
    echo "❌ 错误: Node.js 版本过低，需要 Node.js 16 或更高版本"
    exit 1
fi

echo "✅ Node.js 版本检查通过: $(node -v)"
echo ""

# 进入前端目录
cd frontend || exit

# 检查是否已安装依赖
if [ ! -d "node_modules" ]; then
    echo "📦 未找到依赖，开始安装..."
    npm install

    if [ $? -ne 0 ]; then
        echo "❌ 依赖安装失败，请检查错误信息"
        exit 1
    fi
    echo "✅ 依赖安装成功"
    echo ""
fi

# 启动开发服务器
echo "🚀 启动前端开发服务器 (H5)..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "访问地址: http://localhost:8081"
echo ""
npm run dev:h5
