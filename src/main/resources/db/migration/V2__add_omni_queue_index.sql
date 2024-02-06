CREATE INDEX IF NOT EXISTS idx_omni_queue_omni_type_id_archived_id
    ON test_data_storage.omni_queue (omni_type_id, archived, id);
