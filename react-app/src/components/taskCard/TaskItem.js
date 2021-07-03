import {Box, Chip, Divider, IconButton, makeStyles, Typography} from "@material-ui/core";
import {PlayArrow, Stop} from "@material-ui/icons";
import {useMutation, useQueryClient} from "react-query";
import {startEventNow, stopEvent} from "../../api/events";
import {intervalToDuration} from "date-fns";
import {useEffect, useState} from "react";
import {useInterval} from "react-use";
import {GET_TASKS} from "../../api/tasks";

const useStyles = makeStyles(theme => ({
  flexGrow: {
    flexGrow: 1
  },
  row: {
    display: "flex",
    alignItems: "center",
    paddingBottom: theme.spacing(0.5),
    paddingTop: theme.spacing(0.5)
  },
  chip: {
    marginRight: "1em"
  }
}))

const formatSegment = (segment) => {
  if (segment < 10) return '0' + segment
  else return segment
}

const formatDuration = (duration) =>
  `${formatSegment(duration.hours)}:${formatSegment(duration.minutes)}:${formatSegment(duration.seconds)}`

const TaskItem = ({task}) => {
  const classes = useStyles()
  const [localDurationOffset, setLocalDurationOffset] = useState(0)
  const queryClient = useQueryClient()

  useInterval(() => setLocalDurationOffset(localDurationOffset + 1), task.running ? 1000 : null)

  useEffect(() => setLocalDurationOffset(0), [task])

  const startEventQuery = useMutation(startEventNow, {
    onSuccess: () => {
      queryClient.invalidateQueries(GET_TASKS)
    }
  })

  const stopEventQuery = useMutation(stopEvent, {
    onSuccess: () => {
      queryClient.invalidateQueries(GET_TASKS)
    }
  })

  const handlePlayClicked = () => {
    startEventQuery.mutate(task.id)
  }

  const handleStopClicked = () => {
    stopEventQuery.mutate(null)
  }

  const duration = formatDuration(intervalToDuration({start: 0, end: (task.duration + localDurationOffset) * 1000}))

  return (
    <>
      <Box className={classes.row}>
        <Typography className={classes.flexGrow}>{task.name}</Typography>
        {task.running &&
        <Chip className={classes.chip} label={"running"} size={"small"} color={"primary"} variant={"outlined"}/>}
        <Typography>{duration}</Typography>
        {!task.running &&
        <IconButton onClick={handlePlayClicked}>
          <PlayArrow/>
        </IconButton>}
        {task.running &&
        <IconButton onClick={handleStopClicked}>
          <Stop/>
        </IconButton>}
      </Box>
      <Divider/>
    </>
  )
}

export default TaskItem
