package config

import (
	"anti_fraud/internal/model"
	"os"
	"sync"

	"gopkg.in/yaml.v3"
)

type Config struct {
	LimitConfig model.LimitConfig `yaml:"limit_config"`
	RiskConfig  model.RiskConfig  `yaml:"risk_config"`
}

var (
	once     sync.Once
	instance *Config
)

func Load(configPath string) (*Config, error) {
	var err error
	once.Do(func() {
		var file []byte
		file, err = os.ReadFile(configPath)
		if err != nil {
			return
		}

		instance = &Config{}
		err = yaml.Unmarshal(file, instance)
	})

	return instance, err
}

func Get() *Config {
	return instance
}
