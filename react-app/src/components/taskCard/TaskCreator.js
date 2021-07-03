import {Box, CircularProgress, makeStyles, TextField, Typography} from "@material-ui/core";
import {Autocomplete, createFilterOptions} from "@material-ui/lab";
import {useState} from "react";
import apiCall from "../../apiCall";
import {useMutation, useQuery, useQueryClient} from "react-query";
import {useDebounce} from "react-use";
import {addTask, createTask, GET_TASKS} from "../../api/tasks";

const useStyles = makeStyles((theme) => ({
  fullWidth: {
    width: "100%"
  }
}))

const search = async ({queryKey}) => {
  const [, searchText] = queryKey
  const res = await apiCall("/api/tasks?query=" + searchText)
  return await res.json()
}

const TaskCreator = () => {

  const classes = useStyles()
  const filter = createFilterOptions();
  const [open, setOpen] = useState(false);
  const [searchText, setSearchText] = useState("")
  const [searchTextQuery, setSearchTextQuery] = useState("")
  const queryClient = useQueryClient()

  useDebounce(() => {
    setSearchTextQuery(searchText)
  }, 500, [searchText])

  const searchTasksQuery = useQuery(["searchTasks", searchTextQuery], search)

  const addTaskQuery = useMutation(addTask, {
    onSuccess: () => {
      queryClient.invalidateQueries(GET_TASKS)
    },
    onSettled: () => {
      setSearchText("")
    }
  })

  const createTaskQuery = useMutation(createTask, {
    onSuccess: (createdTask) => {
      console.log(createdTask)
      addTaskQuery.mutate(createdTask)
    }
  })

  const handleOnChange = (event, newValue) => {
    if (newValue && newValue.inputValue) {
      createTaskQuery.mutate(newValue.inputValue)
    } else if (newValue) {
      addTaskQuery.mutate(newValue)
    }
    setSearchText("")
  }

  const handleFilterOptions = (options, params) => {
    const filtered = filter(options, params);
    if (params.inputValue !== "") {
      filtered.push({name: `Create "${params.inputValue}"`, inputValue: params.inputValue})
    }
    return filtered
  }

  return (
    <Box>
      <Autocomplete
        renderInput={(params) => <TextField
          label={"Add task"}
          className={classes.fullWidth}
          {...params}
          InputProps={{
            ...params.InputProps,
            endAdornment: (
              <>
                {searchTasksQuery.isLoading ? <CircularProgress color="inherit" size={20}/> : null}
                {params.InputProps.endAdornment}
              </>
            ),
          }}/>}
        options={searchTasksQuery.data?.items ?? []}
        filterOptions={handleFilterOptions}
        getOptionLabel={option => option?.name ?? ""}
        onChange={handleOnChange}
        open={open}
        onOpen={() => setOpen(true)}
        onClose={() => setOpen(false)}
        loading={searchTasksQuery.isLoading}
        inputValue={searchText}
        onInputChange={(event, newValue) => setSearchText(newValue)}
        value={""}
      />
    </Box>
  )
}


export default TaskCreator
