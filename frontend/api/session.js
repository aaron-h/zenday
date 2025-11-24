/**
 * 修行会话相关API
 */
import request from '../utils/request.js'

export default {
  // 创建会话
  createSession(data) {
    return request.post('/sessions', data)
  },

  // 完成会话
  completeSession(id, data) {
    return request.put(`/sessions/${id}/complete`, data)
  },

  // 获取用户会话历史
  getUserSessions(userId) {
    return request.get(`/sessions/user/${userId}`)
  },

  // 获取指定日期的会话
  getSessionsByDate(userId, date) {
    return request.get(`/sessions/user/${userId}/date/${date}`)
  },

  // 获取日期范围内的会话
  getSessionsByDateRange(userId, startDate, endDate) {
    return request.get(`/sessions/user/${userId}/range`, {
      startDate,
      endDate
    })
  }
}
