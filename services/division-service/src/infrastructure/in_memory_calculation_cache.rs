use crate::domain::{CalculationCache, CalculationRequest, CalculationResult};
use std::collections::HashMap;
use std::sync::Mutex;

pub struct InMemoryCalculationCache {
    cache: Mutex<HashMap<String, CalculationResult>>,
}

impl InMemoryCalculationCache {
    pub fn new() -> Self {
        Self {
            cache: Mutex::new(HashMap::new()),
        }
    }

    fn generate_key(&self, request: &CalculationRequest) -> String {
        format!("divide:{}:{}", request.operand1, request.operand2)
    }
}

impl CalculationCache for InMemoryCalculationCache {
    fn get(&self, request: &CalculationRequest) -> Option<CalculationResult> {
        let key = self.generate_key(request);
        let cache = self.cache.lock().unwrap();
        cache.get(&key).cloned()
    }

    fn store(&self, request: &CalculationRequest, result: &CalculationResult) {
        let key = self.generate_key(request);
        let mut cache = self.cache.lock().unwrap();
        cache.insert(key, result.clone());
    }
}