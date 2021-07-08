import {TimePicker} from "@material-ui/pickers";
import {Box, CircularProgress, IconButton, makeStyles} from "@material-ui/core";
import {useMutation, useQueryClient} from "react-query";
import {GET_TASKS} from "../../api/tasks";
import {GET_EVENTS, splitSession} from "../../api/events";
import {toast} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import {CallSplit} from "@material-ui/icons";
import clsx from 'clsx'


const useStyles = makeStyles((theme) => ({
  picker: {
    fontSize: 14,
    width: "3rem",
    "&::before": {
      border: "none"
    },
    "& input": {
      padding: "0 0 7px"
    }
  },
  progress: {
    marginRight: theme.spacing(1)
  },
  splitButton: {
    marginTop: '-5px',
    marginRight: theme.spacing(1)
  },
  hover: {
    '& .showOnHover': {
      opacity: 0,
      transition: "200ms all"
    },
    '&:hover .showOnHover': {
      opacity: 1
    }
  }
}))

const EventTimePicker = ({selectedTime, eventId, updateFunction, splitButton}) => {

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

  const splitSessionQuery = useMutation(splitSession, {
    onSuccess: () => {
      queryClient.invalidateQueries(GET_TASKS)
      queryClient.invalidateQueries(GET_EVENTS)
    },
    onError: () => {
      toast.error("Unable to split session")
    }
  })

  const handleChange = (newTime) => {
    updateEventQuery.mutate({eventId: eventId, dateTime: newTime})
  }

  const handleSplitSession = () => {
    splitSessionQuery.mutate(eventId)
  }


  return (
    <Box className={classes.hover}>
      {updateEventQuery.isLoading && <CircularProgress size={12} className={classes.progress}/>}
      {splitButton &&
      <IconButton size={"small"} className={clsx(classes.splitButton, 'showOnHover')} onClick={handleSplitSession}>
        <CallSplit/>
      </IconButton>}

      <TimePicker
        ampm={false}
        value={selectedTime}
        onChange={handleChange}
        InputProps={{className: classes.picker}}
      />
    </Box>
  )
}

export default EventTimePicker