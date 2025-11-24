/**
 * 计划相关API
 */
import request from '../utils/request.js'

export default {
  // 获取每日计划
  getDailyPlan(userId, date) {
    return request.get(`/plans/user/${userId}/date/${date}`)
  },

  // 保存每日计划
  saveDailyPlan(data) {
    return request.post('/plans', data)
  },

  // 批量生成计划
  batchGeneratePlans(userId, baseDate, days) {
    return request.post(`/plans/user/${userId}/batch`, null, {
      params: { baseDate, days }
    })
  },

  // 标记计划项完成
  markPlanItemCompleted(itemId) {
    return request.put(`/plans/items/${itemId}/complete`)
  }
}
