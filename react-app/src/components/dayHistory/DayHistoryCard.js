import { Box, makeStyles, Typography } from '@material-ui/core'
import { useQuery } from 'react-query'
import { GET_EVENTS, getEvents, updateEventEnd, updateEventStart } from '../../api/events'
import {
  Timeline,
  TimelineConnector,
  TimelineContent,
  TimelineDot,
  TimelineItem,
  TimelineOppositeContent,
  TimelineSeparator
} from '@material-ui/lab'
import EventTimePicker from './EventTimePicker'

const useStyles = makeStyles(() => ({
  taskName: {
    paddingTop: '2em'
  }
}))

const getStartTimePicker = (ev) => (
  <EventTimePicker
    selectedTime={new Date(ev.start * 1000)}
    eventId={ev.id}
    updateFunction={updateEventStart}
  />
)

const getEndTimePicker = (ev) => (
  <EventTimePicker
    selectedTime={new Date(ev.end * 1000)}
    eventId={ev.id}
    updateFunction={updateEventEnd}
  />
)

const DayHistoryCard = ({ selectedDate }) => {
  const classes = useStyles()

  const getEventsQuery = useQuery([GET_EVENTS, selectedDate], getEvents)

  return (
    <Box m={3}>
      <Typography variant={'h6'}>Day history</Typography>

      {getEventsQuery.isSuccess &&
        getEventsQuery.data.sessions.map((session) => (
          <Timeline>
            {session.events.map((event) => (
              <TimelineItem>
                <TimelineOppositeContent>
                  <Box>{getStartTimePicker(event)}</Box>
                </TimelineOppositeContent>
                <TimelineSeparator>
                  <TimelineDot />
                  <TimelineConnector />
                </TimelineSeparator>
                <TimelineContent>
                  <Box className={classes.taskName}>{event.taskName}</Box>
                </TimelineContent>
              </TimelineItem>
            ))}
            <TimelineItem>
              <TimelineOppositeContent>
                {session.events[session.events.length - 1].end
                  ? getEndTimePicker(session.events[session.events.length - 1])
                  : 'Now'}
              </TimelineOppositeContent>
              <TimelineSeparator>
                <TimelineDot />
              </TimelineSeparator>
              <TimelineContent> </TimelineContent>
            </TimelineItem>
          </Timeline>
        ))}
    </Box>
  )
}

export default DayHistoryCard
