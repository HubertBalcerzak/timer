import apiCall from '../apiCall'

const startEventNow = async (taskId) => {
  await apiCall('/api/events/start', {
    method: 'POST',
    body: JSON.stringify({
      taskId: taskId,
      day: new Date().toISOString()
    })
  })
}

const stopEvent = async () => {
  await apiCall('/api/events/end', {
    method: 'POST'
  })
}

const GET_EVENTS = 'getEvents'

const getEvents = async ({ queryKey }) => {
  const [, date] = queryKey
  const response = await apiCall('/api/events?date=' + date.toISOString())
  return await response.json()
}

const updateEventStart = async ({ eventId, dateTime }) => {
  await apiCall(`/api/events/${eventId}`, {
    method: 'POST',
    body: JSON.stringify({
      start: dateTime.toISOString()
    })
  })
}

const updateEventEnd = async ({ eventId, dateTime }) => {
  await apiCall(`/api/events/${eventId}`, {
    method: 'POST',
    body: JSON.stringify({
      end: dateTime.toISOString()
    })
  })
}

export { startEventNow, stopEvent, GET_EVENTS, getEvents, updateEventStart, updateEventEnd }
