import {Box, Divider, IconButton, makeStyles, Typography} from "@material-ui/core";
import {PlayArrow} from "@material-ui/icons";
import {useMutation} from "react-query";
import {startEventNow} from "../../api/events";

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

const TaskItem = ({taskName, taskId}) => {
  const classes = useStyles()

  const startEvent = useMutation(startEventNow)

  const handlePlayClicked = () => {
    startEvent.mutate(taskId)
  }

  return (
    <>
      <Box className={classes.row}>
        <Typography className={classes.flexGrow}>{taskName}</Typography>
        <IconButton onClick={handlePlayClicked}>
          <PlayArrow/>
        </IconButton>
      </Box>
      <Divider/>
    </>
  )
}

export default TaskItem
