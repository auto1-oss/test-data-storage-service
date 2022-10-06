import {Box, CircularProgress, Sheet, TextField, Typography} from "@mui/joy";
import Header from "../components/Header";
import React, {useEffect, useState, useCallback} from "react";
import {useTestDataService} from "../api/useTestDataService";
import DataViewModal from "../components/DataViewModal";
import SearchIcon from '@mui/icons-material/Search';
import QueueList from "../components/QueueList";

const styles = {
    container: {
        width: '100%', display: 'flex', justifyContent: 'center', padding: 4
    },
    queueList: {
        borderRadius: 'sm',
        display: 'grid',
        p: 4,
        gap: 2,
        bgcolor: 'background.componentBg'
    }
}

export const HomePage = () => {

    const {queues, fetchAllTestDataTypes, queuesLoading, getTestDataType, testData} = useTestDataService()
    const [displayDataViewModal, setDisplayDataViewModal] = useState(false);
    const [searchFilter, setSearchFilter] = React.useState('');

    const handleChange = (event) => {
        setSearchFilter(event.target.value);
    };

    const handleCloseModal = () => {
        setDisplayDataViewModal(false)
    }


    const handleFetchTestData = useCallback(async (testDataType) => {
        await getTestDataType(testDataType)
        setDisplayDataViewModal(true)
    }, []);

    useEffect(() => {
        fetchAllTestDataTypes()
    }, []);

    return (
        <Box>
            <Header/>
            {queuesLoading &&
                <Box sx={styles.container}>
                    <CircularProgress variant="soft" size={"md"}/>
                </Box>}
            {!queuesLoading && queues.length === 0 &&
                <Box sx={styles.container}>
                    <Typography textColor="neutral.600" fontWeight="lg" level="h4">Data queues are not
                        created</Typography>
                </Box>}
            <Sheet sx={styles.queueList}>
                {!queuesLoading && queues.length > 0 &&
                    <Box width={"20%"}>
                        <TextField
                            onChange={handleChange}
                            value={searchFilter}
                            placeholder="Search..."
                            startDecorator={<SearchIcon/>}
                        />
                    </Box>}
                <QueueList
                    list={queues.filter(item => item.dataType.toLowerCase().includes(searchFilter.toLowerCase()))}
                    fetchData={handleFetchTestData}/>
            </Sheet>
            {displayDataViewModal &&
                <DataViewModal testData={testData} open={displayDataViewModal} close={handleCloseModal}/>}
        </Box>
    )
}