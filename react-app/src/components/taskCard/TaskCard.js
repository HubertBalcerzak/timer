import {Box, Button, Card, makeStyles, Typography} from "@material-ui/core";
import TaskItem from "./TaskItem";
import TaskCreator from "./TaskCreator";
import apiCall from "../../apiCall";

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

  const createTask = (newTaskName) => {
    apiCall("/api/tasks", {method: "POST", body: JSON.stringify({name: newTaskName})})
      .then(r => r.json())
      .then(r => console.log(r))
  }

  return (
    <Box m={3}>
      <Box display={"flex"} className={classes.titleSpacer}>
        <Typography variant={"h6"} className={classes.flexGrow}>Tasks</Typography>
        <Button variant={"contained"} color={"primary"}>Stop</Button>
      </Box>
      <TaskItem taskName={"task1"}/>
      <TaskItem taskName={"task1"}/>
      <TaskItem taskName={"task1"}/>
      <TaskItem taskName={"task1"}/>
      <TaskItem taskName={"task1"}/>
      <TaskCreator addTask={addTask} createTask={createTask}/>
    </Box>
  )
}

export default TaskCard
