import apiCall from '../apiCall'

const GET_TASKS = 'getTasks'

const getTasks = async ({ queryKey }) => {
  const [, date] = queryKey
  const response = await apiCall('/api/tasks/day?date=' + date.toISOString())
  return await response.json()
}

const addTask = async (task) => {
  await apiCall(`/api/tasks/${task.id}/day`, {
    method: 'POST',
    body: JSON.stringify({
      date: new Date().toISOString()
    })
  })
}

const createTask = async (taskName) => {
  const response = await apiCall('/api/tasks', {
    method: 'POST',
    body: JSON.stringify({ name: taskName })
  })
  return await response.json()
}

export { GET_TASKS, getTasks, addTask, createTask }
