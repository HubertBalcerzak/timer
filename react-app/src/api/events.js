import apiCall from "../apiCall";

const startEventNow = async (taskId) => {
  await apiCall("/api/events/start", {
    method: "POST",
    body: JSON.stringify({
      taskId: taskId,
      day: new Date().toISOString()
    })
  })
}

const stopEvent = async () => {
  await apiCall("/api/events/end", {
    method: "POST"
  })
}

export {startEventNow, stopEvent}