import {Box, Button, Card, makeStyles, Typography} from "@material-ui/core";
import TaskItem from "./TaskItem";
import TaskCreator from "./TaskCreator";
import apiCall from "../../apiCall";
import {useQuery} from "react-query";
import {GET_TASKS_TODAY, getTasksToday} from "../../api/tasks";

const useStyles = makeStyles(theme => ({
  flexGrow: {
    flexGrow: 1
  },
  titleSpacer: {
    marginBottom: theme.spacing(2)
  }
}))

const TaskCard = () => {
  const classes = useStyles()

  const addTask = (task) => {
    console.log(task)
  }

  const tasks = useQuery(GET_TASKS_TODAY, getTasksToday)

  return (
    <Box m={3}>
      <Box display={"flex"} className={classes.titleSpacer}>
        <Typography variant={"h6"} className={classes.flexGrow}>Tasks</Typography>
        <Button variant={"contained"} color={"primary"}>Stop</Button>
      </Box>
      {tasks.isSuccess && tasks.data.map(task => <TaskItem taskName={task.name} key={task.id}/>)}
      <TaskCreator addTask={addTask}/>
    </Box>
  )
}

export default TaskCard
