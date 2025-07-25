package infrastructure

import (
	"fmt"
	"multiplication-service/internal/domain"
	"sync"
)

type InMemoryCalculationCache struct {
	cache map[string]*domain.CalculationResult
	mutex sync.RWMutex
}

func NewInMemoryCalculationCache() *InMemoryCalculationCache {
	return &InMemoryCalculationCache{
		cache: make(map[string]*domain.CalculationResult),
	}
}

func (c *InMemoryCalculationCache) Get(request *domain.CalculationRequest) *domain.CalculationResult {
	c.mutex.RLock()
	defer c.mutex.RUnlock()
	
	key := c.generateKey(request)
	return c.cache[key]
}

func (c *InMemoryCalculationCache) Store(request *domain.CalculationRequest, result *domain.CalculationResult) {
	c.mutex.Lock()
	defer c.mutex.Unlock()
	
	key := c.generateKey(request)
	c.cache[key] = result
}

func (c *InMemoryCalculationCache) generateKey(request *domain.CalculationRequest) string {
	return fmt.Sprintf("multiply:%s:%s", request.Operand1.String(), request.Operand2.String())
}