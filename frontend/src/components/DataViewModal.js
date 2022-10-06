import * as React from 'react';
import Modal from '@mui/joy/Modal';
import ModalClose from '@mui/joy/ModalClose';
import Typography from '@mui/joy/Typography';
import Sheet from '@mui/joy/Sheet';
import {Box, IconButton} from "@mui/joy";
import ContentCopyIcon from '@mui/icons-material/ContentCopy';
import DoneIcon from '@mui/icons-material/Done';
import {useState} from "react";

export default function DataViewModal({open, close, testData}) {

    const [copied, setCopied] = useState(false)

    const handleClickCopyToClipboard = () => {
        navigator.clipboard.writeText(JSON.stringify(testData, null, 2))
        setCopied(true)
    }

    return (
            <Modal
                aria-labelledby="modal-title"
                aria-describedby="modal-desc"
                open={open}
                onClose={close}
                sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center'}}
            >
                <Sheet
                    variant="outlined"
                    sx={{
                        maxHeight: '80vh',
                        maxWidth: 500,
                        borderRadius: 'md',
                        p: 3,
                        boxShadow: 'lg',
                    }}
                >
                    <ModalClose
                        variant="outlined"
                        sx={{
                            top: 'calc(-1/4 * var(--IconButton-size))',
                            right: 'calc(-1/4 * var(--IconButton-size))',
                            boxShadow: '0 2px 12px 0 rgba(0 0 0 / 0.2)',
                            borderRadius: '50%',
                            bgcolor: 'background.body',
                        }}
                    />
                    <Typography
                        component="h2"
                        id="modal-title"
                        level="h4"
                        textColor="inherit"
                        fontWeight="lg"
                        mb={1}
                    >
                        Test Data View
                    </Typography>
                    <Box display={"flex"} justifyContent={"flex-end"} >
                        <IconButton
                            color="neutral"
                            sx={{position: 'absolute', right: '35px', top: '75px'}}
                            onClick={handleClickCopyToClipboard}
                            size="sm"
                            variant="plain"
                        >
                            {copied ? <DoneIcon/> : <ContentCopyIcon/>}
                        </IconButton>
                    </Box>
                    <Box sx={{backgroundColor: 'neutral.200', overflow: 'auto', maxHeight: '70vh', p: 2, borderRadius: '8px'}}>
                         <pre >
                               {JSON.stringify(testData, null, 2)}
                            </pre>
                    </Box>
                </Sheet>
            </Modal>
    );
}
