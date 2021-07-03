import {Box, Divider, IconButton, makeStyles, Typography} from "@material-ui/core";
import {PlayArrow} from "@material-ui/icons";
import {useMutation, useQueryClient} from "react-query";
import {startEventNow} from "../../api/events";
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

  const startEvent = useMutation(startEventNow, {
    onSuccess: () => {
      queryClient.invalidateQueries(GET_TASKS)
    }
  })

  const handlePlayClicked = () => {
    startEvent.mutate(task.id)
  }

  const duration = formatDuration(intervalToDuration({start: 0, end: (task.duration + localDurationOffset) * 1000}))

  return (
    <>
      <Box className={classes.row}>
        <Typography className={classes.flexGrow}>{task.name}</Typography>
        <Typography>{duration}</Typography>
        <IconButton onClick={handlePlayClicked}>
          <PlayArrow/>
        </IconButton>
      </Box>
      <Divider/>
    </>
  )
}

export default TaskItem
