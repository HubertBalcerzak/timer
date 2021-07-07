import { KeyboardDatePicker } from '@material-ui/pickers'

const DatePicker = ({ selectedDate, setSelectedDate }) => {
  return (
    <KeyboardDatePicker
      disableToolbar
      variant='inline'
      format='MM/dd/yyyy'
      margin='normal'
      id='date-picker-inline'
      label='Date'
      value={selectedDate}
      onChange={setSelectedDate}
      KeyboardButtonProps={{
        'aria-label': 'change date'
      }}
    />
  )
}

export default DatePicker
