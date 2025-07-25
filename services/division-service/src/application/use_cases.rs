use crate::domain::{
    CalculationCache, CalculationRequest, CalculationResult, DivisionCalculator, InputValidator,
};
use std::sync::Arc;
use std::time::Instant;

#[derive(Clone)]
pub struct ExecuteDivisionUseCase {
    validator: Arc<InputValidator>,
    calculator: Arc<DivisionCalculator>,
    cache: Arc<dyn CalculationCache>,
}

impl ExecuteDivisionUseCase {
    pub fn new(
        validator: Arc<InputValidator>,
        calculator: Arc<DivisionCalculator>,
        cache: Arc<dyn CalculationCache>,
    ) -> Self {
        Self {
            validator,
            calculator,
            cache,
        }
    }

    pub fn execute(&self, request: &CalculationRequest) -> Result<CalculationResult, String> {
        let start = Instant::now();

        self.validator.validate(request)?;

        if let Some(cached_result) = self.cache.get(request) {
            let processing_time = start.elapsed();
            return Ok(CalculationResult::new(
                cached_result.result,
                processing_time,
                true,
            ));
        }

        let result = self.calculator.divide(request.operand1, request.operand2)?;
        let processing_time = start.elapsed();

        let calculation_result = CalculationResult::new(result, processing_time, false);

        self.cache.store(request, &calculation_result);

        Ok(calculation_result)
    }
}