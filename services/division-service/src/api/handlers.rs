use crate::application::ExecuteDivisionUseCase;
use crate::domain::CalculationRequest;
use actix_web::{web, HttpResponse, Result};
use chrono::{DateTime, Utc};
use serde::{Deserialize, Serialize};
use std::sync::Mutex;

#[derive(Clone)]
pub struct DivisionHandler {
    use_case: ExecuteDivisionUseCase,
    last_calculation: std::sync::Arc<Mutex<Option<DateTime<Utc>>>>,
}

#[derive(Serialize, Deserialize)]
pub struct HealthResponse {
    pub status: String,
    pub cache: String,
    #[serde(rename = "lastCalculation")]
    pub last_calculation: Option<DateTime<Utc>>,
}

impl DivisionHandler {
    pub fn new(use_case: ExecuteDivisionUseCase) -> Self {
        Self {
            use_case,
            last_calculation: std::sync::Arc::new(Mutex::new(None)),
        }
    }
}

pub async fn calculate(
    handler: web::Data<DivisionHandler>,
    request: web::Json<CalculationRequest>,
) -> Result<HttpResponse> {
    match handler.use_case.execute(&request) {
        Ok(result) => {
            let mut last_calc = handler.last_calculation.lock().unwrap();
            *last_calc = Some(Utc::now());
            Ok(HttpResponse::Ok().json(result))
        }
        Err(error) => Ok(HttpResponse::BadRequest().json(serde_json::json!({
            "error": error
        }))),
    }
}

pub async fn health(handler: web::Data<DivisionHandler>) -> Result<HttpResponse> {
    let last_calc = handler.last_calculation.lock().unwrap().clone();
    let response = HealthResponse {
        status: "UP".to_string(),
        cache: "UP".to_string(),
        last_calculation: last_calc,
    };
    Ok(HttpResponse::Ok().json(response))
}