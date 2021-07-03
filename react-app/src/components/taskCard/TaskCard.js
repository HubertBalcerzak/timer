import {Box, Button, makeStyles, Typography} from "@material-ui/core";
import TaskItem from "./TaskItem";
import TaskCreator from "./TaskCreator";
import {useMutation, useQuery} from "react-query";
import {GET_TASKS, getTasks} from "../../api/tasks";
import {stopEvent} from "../../api/events";
import {useInterval} from "react-use";

const useStyles = makeStyles(theme => ({
  flexGrow: {
    flexGrow: 1
  },
  titleSpacer: {
    marginBottom: theme.spacing(2)
  }
}))

const TaskCard = ({selectedDate}) => {
  const classes = useStyles()

  const addTask = (task) => {
    console.log(task)
  }

  const tasks = useQuery([GET_TASKS, selectedDate], getTasks)

  const stopEventQuery = useMutation(stopEvent)

  const handleOnStop = () => {
    stopEventQuery.mutate(undefined)
  }

  return (
    <Box m={3}>
      <Box display={"flex"} className={classes.titleSpacer}>
        <Typography variant={"h6"} className={classes.flexGrow}>Tasks</Typography>
        <Button variant={"contained"} color={"primary"} onClick={handleOnStop}>Stop</Button>
      </Box>
      {tasks.isSuccess && tasks.data.map(task => <TaskItem task={task} key={task.id}/>)}
      <TaskCreator addTask={addTask}/>
    </Box>
  )
}

export default TaskCard
