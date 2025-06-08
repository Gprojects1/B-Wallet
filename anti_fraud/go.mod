module anti_fraud

go 1.23.3

require (
	google.golang.org/grpc v1.73.0
	google.golang.org/protobuf v1.36.6
	gopkg.in/yaml.v3 v3.0.1
	gorm.io/gorm v1.30.0
)

require (
	github.com/Gprojects1/B-wallet-common-proto v0.0.0-local
	github.com/jinzhu/inflection v1.0.0 // indirect
	github.com/jinzhu/now v1.1.5 // indirect
	golang.org/x/net v0.38.0 // indirect
	golang.org/x/sys v0.31.0 // indirect
	golang.org/x/text v0.23.0 // indirect
	google.golang.org/genproto/googleapis/rpc v0.0.0-20250324211829-b45e905df463 // indirect
)

replace github.com/Gprojects1/B-wallet-common-proto => ../proto
