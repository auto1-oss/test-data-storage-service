import {useState} from "react";
import {BASE_URL_API} from "./constants";
import axios from "axios";

export function useTestDataService() {

    const [queues, setQueues] = useState([]);
    const [testData, setTestData] = useState([]);
    const [queuesLoading, setQueuesLoading] = useState(false);

    async function fetchAllTestDataTypes(payload) {
        setQueuesLoading(true);
        await axios.get(`${BASE_URL_API}/queue/omni/count`, payload).then(res => {
            setQueues(res.data)
            setQueuesLoading(false);
        }).catch(function (error) {
            console.log(error)
            setQueuesLoading(true);
        });
    }

    async function getTestDataType(testDataType) {
        await axios.get(`${BASE_URL_API}/queue/omni/${testDataType}`).then(res => {
            setTestData(res.data)
        }).catch(function (error) {
            console.log(error)
        });
    }



    return {
        queues,
        testData,
        fetchAllTestDataTypes,
        getTestDataType,
        queuesLoading
    };
}
