import MenuBar from "../components/MenuBar";
import {Box, Card, makeStyles} from "@material-ui/core";
import DayHistoryCard from "../components/dayHistory/DayHistoryCard";
import TaskCard from "../components/taskCard/TaskCard";
import {useEffect} from "react";
import apiCall from "../apiCall";

const useStyles = makeStyles(({
  container: {
    display: "flex",
    justifyContent: "space-between",
    gap: "10vw",
    marginRight: "10vw",
    marginLeft: "10vw",
    flexWrap: "wrap"
  },
  card: {
    flexGrow: 1,
    minWidth: "30em"
  }
}))

const Dashboard = () => {
  const classes = useStyles()

  useEffect(() => {//todo reload page if user created
    apiCall("/api/users", {method: "POST"})
  }, [])

  return (
    <>
      <MenuBar/>
      <Box className={classes.container} mt={4}>
        <Card className={classes.card}>
          <TaskCard/>
        </Card>
        <Card className={classes.card}>
          <DayHistoryCard/>
        </Card>
      </Box>
    </>
  )
}

export default Dashboard
