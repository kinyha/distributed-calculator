mod domain;
mod application;
mod infrastructure;
mod api;

use actix_web::{web, App, HttpServer, middleware::Logger};
use api::handlers::DivisionHandler;
use application::use_cases::ExecuteDivisionUseCase;
use domain::{DivisionCalculator, InputValidator};
use infrastructure::InMemoryCalculationCache;
use std::sync::Arc;

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    env_logger::init();

    let calculator = Arc::new(DivisionCalculator::new());
    let validator = Arc::new(InputValidator::new());
    let cache = Arc::new(InMemoryCalculationCache::new());
    
    let use_case = ExecuteDivisionUseCase::new(
        validator.clone(),
        calculator.clone(),
        cache.clone(),
    );
    
    let handler = DivisionHandler::new(use_case);

    println!("Division service starting on port 8084");

    HttpServer::new(move || {
        App::new()
            .wrap(Logger::default())
            .app_data(web::Data::new(handler.clone()))
            .route("/calculate", web::post().to(api::handlers::calculate))
            .route("/health", web::get().to(api::handlers::health))
    })
    .bind("0.0.0.0:8084")?
    .run()
    .await
}