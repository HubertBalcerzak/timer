import MenuBar from '../components/MenuBar'
import { Box, Card, makeStyles } from '@material-ui/core'
import DayHistoryCard from '../components/dayHistory/DayHistoryCard'
import TaskCard from '../components/taskCard/TaskCard'
import { useEffect, useState } from 'react'
import apiCall from '../apiCall'
import DatePicker from '../components/DatePicker'

const useStyles = makeStyles({
  container: {
    display: 'flex',
    marginRight: '10vw',
    marginLeft: '10vw',
    flexDirection: 'column'
  },
  panelContainer: {
    display: 'flex',
    justifyContent: 'space-between',
    gap: '10vw',
    flexWrap: 'wrap'
  },
  card: {
    flexGrow: 1,
    minWidth: '30em'
  }
})

const Dashboard = () => {
  const classes = useStyles()
  const [selectedDate, setSelectedDate] = useState(new Date())
  useEffect(() => {
    //todo reload page if user created
    apiCall('/api/users', { method: 'POST' })
  }, [])

  return (
    <>
      <MenuBar />
      <Box mt={4} className={classes.container}>
        <Box>
          <DatePicker selectedDate={selectedDate} setSelectedDate={setSelectedDate} />
        </Box>
        <Box className={classes.panelContainer} mt={4} mb={4}>
          <Card className={classes.card}>
            <TaskCard selectedDate={selectedDate} />
          </Card>
          <Card className={classes.card}>
            <DayHistoryCard selectedDate={selectedDate} />
          </Card>
        </Box>
      </Box>
    </>
  )
}

export default Dashboard
