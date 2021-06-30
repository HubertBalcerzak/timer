import apiCall from "../apiCall";

const GET_TASKS_TODAY = "getTasksToday"

const getTasksToday = async (queryKey) => {
  const response = await apiCall("/api/tasks/day?date=" + new Date().toISOString())
  return await response.json()
}

const addTask = async (task) => {
  await apiCall(`/api/tasks/${task.id}/day`, {
    method: "POST",
    body: JSON.stringify({
      date: new Date().toISOString()
    })
  })
}


const createTask = async (taskName) => {
  const response = await apiCall("/api/tasks", {method: "POST", body: JSON.stringify({name: taskName})})
  return await response.json()
}

export {GET_TASKS_TODAY, getTasksToday, addTask, createTask}