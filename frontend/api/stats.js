/**
 * 统计相关API
 */
import request from '../utils/request.js'

export default {
  // 获取统计数据
  getStats(userId, range = 'week') {
    return request.get(`/stats/user/${userId}`, { range })
  }
}
