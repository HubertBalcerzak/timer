import {Box, Button, CircularProgress, makeStyles, Typography} from "@material-ui/core";
import TaskItem from "./TaskItem";
import TaskCreator from "./TaskCreator";
import {useMutation, useQuery, useQueryClient} from "react-query";
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
  const queryClient = useQueryClient()

  const addTask = (task) => {
    console.log(task)
  }

  const tasks = useQuery([GET_TASKS, selectedDate], getTasks)
  const anyTaskRunning = tasks.data.some((task) => task.running)

  const stopEventQuery = useMutation(stopEvent, {
    onSuccess: () => {
      queryClient.invalidateQueries(GET_TASKS)
    }
  })

  const handleOnStop = () => {
    stopEventQuery.mutate(null)
  }

  return (
    <Box m={3}>
      <Box display={"flex"} className={classes.titleSpacer}>
        <Typography variant={"h6"} className={classes.flexGrow}>Tasks</Typography>
        <Box display={"flex"} alignItems={"center"} mr={1}>
          {tasks.isLoading && <CircularProgress size={25}/>}
        </Box>
        <Button variant={"contained"} color={"primary"} onClick={handleOnStop} disabled={!anyTaskRunning}>Stop</Button>
      </Box>
      {tasks.isSuccess && tasks.data.map(task => <TaskItem task={task} key={task.id}/>)}
      <Box mt={1}>
        <TaskCreator addTask={addTask}/>
      </Box>
    </Box>
  )
}

export default TaskCard
