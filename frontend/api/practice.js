/**
 * 功课相关API
 */
import request from '../utils/request.js'

export default {
  // 获取用户功课列表
  getUserPractices(userId, enabledOnly = true) {
    return request.get(`/practices/user/${userId}`, { enabledOnly })
  },

  // 获取功课详情
  getPractice(id) {
    return request.get(`/practices/${id}`)
  },

  // 创建功课
  createPractice(data) {
    return request.post('/practices', data)
  },

  // 更新功课
  updatePractice(id, data) {
    return request.put(`/practices/${id}`, data)
  },

  // 删除功课
  deletePractice(id) {
    return request.delete(`/practices/${id}`)
  },

  // 获取推荐功课
  createRecommendedPractices(userId) {
    return request.post(`/practices/user/${userId}/recommended`)
  }
}
