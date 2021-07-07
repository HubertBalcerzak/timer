import { TimePicker } from '@material-ui/pickers'
import { CircularProgress, makeStyles } from '@material-ui/core'
import { useMutation, useQueryClient } from 'react-query'
import { GET_TASKS } from '../../api/tasks'
import { GET_EVENTS } from '../../api/events'
import { toast } from 'react-toastify'
import 'react-toastify/dist/ReactToastify.css'

const useStyles = makeStyles((theme) => ({
  picker: {
    fontSize: 14,
    width: '4em',
    '&::before': {
      border: 'none'
    }
  },
  progress: {
    marginRight: theme.spacing(1)
  }
}))

const EventTimePicker = ({ selectedTime, eventId, updateFunction }) => {
  const classes = useStyles()
  const queryClient = useQueryClient()

  const updateEventQuery = useMutation(updateFunction, {
    onSuccess: () => {
      queryClient.invalidateQueries(GET_TASKS)
      queryClient.invalidateQueries(GET_EVENTS)
    },
    onError: () => {
      toast.error("Tasks can't overlap")
    }
  })

  const handleChange = (newTime) => {
    updateEventQuery.mutate({ eventId: eventId, dateTime: newTime })
  }

  return (
    <>
      {updateEventQuery.isLoading && <CircularProgress size={12} className={classes.progress} />}
      <TimePicker
        classesName={classes.picker}
        ampm={false}
        value={selectedTime}
        onChange={handleChange}
        InputProps={{ className: classes.picker }}
      />
    </>
  )
}

export default EventTimePicker
