CREATE TABLE calculation_history (
    id BIGSERIAL PRIMARY KEY,
    request_id VARCHAR(255) NOT NULL UNIQUE,
    operand1 DECIMAL(20,10) NOT NULL,
    operand2 DECIMAL(20,10) NOT NULL,
    operation VARCHAR(20) NOT NULL,
    result DECIMAL(20,10) NOT NULL,
    processing_time_ms BIGINT NOT NULL,
    cached BOOLEAN NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    service_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_calculation_history_operation ON calculation_history(operation);
CREATE INDEX idx_calculation_history_timestamp ON calculation_history(timestamp);
CREATE INDEX idx_calculation_history_request_id ON calculation_history(request_id);