import {Box, Card, makeStyles} from "@material-ui/core";
import DatePicker from "../DatePicker";
import TaskCard from "../taskCard/TaskCard";
import DayHistoryCard from "../dayHistory/DayHistoryCard";
import {useEffect, useState} from "react";
import apiCall from "../../apiCall";

const useStyles = makeStyles(({
  container: {
    display: "flex",
    marginRight: "10vw",
    marginLeft: "10vw",
    flexDirection: "column"
  },
  panelContainer: {
    display: "flex",
    justifyContent: "space-between",
    gap: "10vw",
    flexWrap: "wrap"
  },
  card: {
    flexGrow: 1,
    minWidth: "30em"
  }
}))

const Today = () => {
  const classes = useStyles()
  const [selectedDate, setSelectedDate] = useState(new Date())
  useEffect(() => {//todo reload page if user created
    apiCall("/api/users", {method: "POST"})
  }, [])

  return (
    <Box mt={4} className={classes.container}>
      <Box>
        <DatePicker selectedDate={selectedDate} setSelectedDate={setSelectedDate}/>
      </Box>
      <Box className={classes.panelContainer} mt={4} mb={4}>
        <Card className={classes.card}>
          <TaskCard selectedDate={selectedDate}/>
        </Card>
        <Card className={classes.card}>
          <DayHistoryCard selectedDate={selectedDate}/>
        </Card>
      </Box>
    </Box>
  )
}

export default Today