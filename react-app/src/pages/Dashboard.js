import MenuBar from "../components/MenuBar";
import {Box, Card, makeStyles} from "@material-ui/core";
import DayHistoryCard from "../components/dayHistory/DayHistoryCard";
import TaskCard from "../components/taskCard/TaskCard";

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
