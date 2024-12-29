# 9장 상품 관리 애플리케이션 만들기

- 데이터 구조 정하기
    - 상품: Prdocut → class
    - 상품 번호: id → 타입: Long
        - 여기에서 상품 번호는 중복된 번호를 가질수 없고, 상품 번호가 Product안에 존재하기 때문에 id라는 통용어를 사용한 것이다.
    - 상품 이름: name → 타입: String
    - 가격: price → 타입 : Integer
    - 재고 수량: amount → 타입: Integer
- 실무에서는 데이터가 Integer의 숫자 범위인 21억 개를 훌쩍 넘어가기 때문에 Long(2의 63제곱)으로 정했다.

## Long vs long

- long : Primitive Type으로 언어 차원에서 지원하는 기본 타입이므로, 특정 클래스가 아닌 순수한 타입이다.
- Long: Wrapper Class로 순수한 타입이 아닌 클래스로서 존재한다.
- 클래스를 사용하는 이유?
    - 해당 레퍼런스 변수가 null 값을 가질수 있다.
    - ArrayList 같은 컬랙션의 요소가 될 수 있다.
- Boxing : Primitive type을 Wrapper class로 넣는것을 말한다.
- Unboxing: Wrapper class를 Primitive type에 넣는 것을 말한다.
- 이렇게 두 개를 왔다가 갔다 해야하는 boxing, unboxing이 자주 일어나면 확인해서 처리해야한다.

- 406 Not Acceptable
    - 콘텐츠 네고시에이션 과정이 실패했을 때 반환하는 상태 코드이다.
    - 이는 Accept 해더에 문제가 있어서 발생
        - 여기에서 발생한 이유는 변수가 private인데 값을 가져올 수 있는 클래스내에 메소드가 없다.
        - 그래서 두가지 해결 방법이 있는ㄴ데 getter,setter를 사용하던가 아니면 private→public으로 바꾸는 경우가 있다.
        - 고친 후 실행 사진
        
        ![스크린샷 2024-12-28 오후 8.23.18.png](9%E1%84%8C%E1%85%A1%E1%86%BC%20%E1%84%89%E1%85%A1%E1%86%BC%E1%84%91%E1%85%AE%E1%86%B7%20%E1%84%80%E1%85%AA%E1%86%AB%E1%84%85%E1%85%B5%20%E1%84%8B%E1%85%A2%E1%84%91%E1%85%B3%E1%86%AF%E1%84%85%E1%85%B5%E1%84%8F%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%89%E1%85%A7%E1%86%AB%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2016a96aed9b3a80bebcc6efa5f21b691c/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2024-12-28_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_8.23.18.png)
        
- json을 인스턴스로 , 인스턴스를 JSON으로 바꿔주는 일은 누가 하는가? → MappingJackson2HttpMessageConverter가 해준다.

- 상품을 리스트에 추가하기
    - 문제점: 전에 즐겨찾기 서비스를 구현할때 사용했던 컨트롤러에는 모든 코드가 controller에 있다. 그럼 다른 컨트롤러가 즐겨찾기 리스트에 접근하려면 controller의 객체 인스턴스를 생성해야한다. 이렇게 하면 서로 다른 각자의 리스트를 가지게 될것이다.
    - 이를 해결하기 위해서 Layered Architecture를 알아봐야한다.
        - 레이어드 아키텍처
            - 하나의 소프트웨어를 여러 개의 계층으로 나누고 각 계층의 책임과 역할을 구분하여 여러가지 제약을 두는 설계 방법이다.
            - 표현(Presentation) 계층
                - interface 계층으로 불리고 클라이언트로부터 들어오는 요청을 받고 응답해 주는 역할을 한다.
            - 응용(Application) 계층
                - 표현 계층에서 넘겨 받은 데이터로 새로운 자원을 저장하거나 저장되어있던 자원을 조회해 온다.
            - 도메인(Domain) 계층
                - 도메인 객체가 위치한다.
            - 인프라스트럭처(Infrastructure)계층
                - 특정 인프라스트럭처에 접근하는 구현 코드들이 위치하는 계층이다. 리스트에 상품을 저장하는 로직이나 데이터베이스에 상품을 저장하는 코드등이 포함되어야 한다.

- 빈 등록과 의존성 주입하기
    - 주입될 의존성(클래스)를 빈(Bean)으로 등록
    - @Service, @RestController, @Repository등이 붙어있으면 자동으로 스프링이 Bean으로 등록해준다.
    - SimpleProductService에 @Service부여함으로써 의존성을 주입해준다.
    - 그리고 @Autowired를 사용하게 되면 스프링에서 자동으로 빈에서 가져와서 사용해준다.
    - @Repository 또한 의존성을 주입해주는 방법이다.
    - CopyOnWriteArrayList<>()는 웹 애플리케이션이 여러 개의 스레드가 동시에 동작하는 멀티 스레드라는 특수한 환경 때문에 ‘스레드 세이프한’ 컬렉션을 사용해야하기 때문이다. ArrayList<>()는 스레드 세이프하지 않다.

- id 추가하기
    - id를 추가하는 방법으로는 Product내에서 static 변수로 관리하는 방법과 ListProductRepository에서 관리하는 방법이 있다. 먼저 Product내에서 static 변수로 관리를 하게 되면 데이터베이스를 사용하도록 애플리케이션을 수정할 때 마다 Product를 변경해야한다. 이렇게 되면 도메인 계층이 외부 요소에 의존하게 되기 때문에 올바르지 않다.
    - ListProductRepository에서 관리하려면 Product에 id값에 setter를 추가해줘야한다.
    - AutomicLong은 CopyOnWriteArrayList<>와 같이 여러 개의 스레드가 동시에 동작하는 멀티스레드에서 스레드 안전을 위해서 사용한다.

- DTO
    - 데이터를 전송하는 역할을 가진 객체이다.
    - 지금처럼 도메인을 그대로 데이터 전송하는 객체로 사용하게 되면 도메인의 구조가 바뀌게 되면 도메인을 매개변수로 사용하는 모든 계층에서 변화를 겪어야 하기에 전송을 할때 따로 사용하는 객체를 만드는데 그것을 DTO라고 부른다.
    - setter만 없으면 ProductDto와 똑같다.
    - DTO를 따로 만들게 되면 이제 Product객체는 더이상 getter를 사용할 일이 없어지게 되어서 getter없애도 된다.

- ModelMapper
    - 자바에서 제공하는 리플렉션 API를 사용하여 두 클래스 사이의 변환 기능을 제공하는 라이브러리이다.
    - 여기에서는 ProductDto 와 Product 를 서로 변환해주기 위해 사용한다.
    - 캡슐화시키기 위해서  만들어준것이다.

- 지금까지의 레이어드 아키텍처 특징
    - 도메인 계층은 다른 계층에 의존하지 않는다.
    - 다른 모든 계층은 인프라스트럭처 계층에 의존하지 않아야 한다.

# 상품 조회하기

- 조회 기능은 총 세가지가 있다.
    - 상품 번호를 기준으로 하나의 상품을 조회할 수 있어야 한다.
    - 전체 상품 목록을 조회할 수 있어야 한다.
    - 상품 이름에 포함된 특정 문자열을 기준으로 검색할 수 있어야 한다.
    
1. 상품 번호를 기준으로 조회 기능 추가하기
    
    ```java
    public Product findById(Long id){
            return  products.stream()
                    .filter(product -> product.sameId(id))
                    .findFirst()
                    .orElseThrow();
        }
    ```
    
    - 번호를 기준으로 filter를 통해서 sameId 메서드를 호출해서 나온 값중 findFirst()를 통해서 첫 번째 Product를 Optional<Product>로 반환해준다. 그래서 orElseThrow()를 통해서 있으면 값을 리턴하= 고 없으면 throw를 던지게 한다.
    - 이제 전송할때 productDto로 넘겨줘야하는데 그 코드를 작성해야한다.
    - SimpleProductService.java
    
    ```java
    public ProductDto findById(Long id){
            Product product = listProductRepository.findById(id);
            ProductDto productDto  = modelMapper.map(product, ProductDto.class);
            return productDto;
        }
    ```
    
    - ProductController.java
    
    ```java
    @RequestMapping(value= "/products/{id}", method = RequestMethod.GET)
        public ProductDto findProductById(@PathVariable Long id)
        {
            return simpleProductService.findById(id);
        }
    ```
    
    - 여기서 Path에 id를 요청받아서 받게 되면 그 id를 가지고 값을 찾아서 return하게 했다.
    - findById로 값을 찾으면 Product클래스로 리턴하기에 ProductDto로 보내주기 위해서는 ModelMapper를 사용해서 옮기면 된다.
    
    ![스크린샷 2024-12-28 오후 9.50.34.png](9%E1%84%8C%E1%85%A1%E1%86%BC%20%E1%84%89%E1%85%A1%E1%86%BC%E1%84%91%E1%85%AE%E1%86%B7%20%E1%84%80%E1%85%AA%E1%86%AB%E1%84%85%E1%85%B5%20%E1%84%8B%E1%85%A2%E1%84%91%E1%85%B3%E1%86%AF%E1%84%85%E1%85%B5%E1%84%8F%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%89%E1%85%A7%E1%86%AB%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2016a96aed9b3a80bebcc6efa5f21b691c/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2024-12-28_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_9.50.34.png)
    
2. 전체 상품 목록 조회 기능 추가하기
    - 먼저 ListProductRepository에서 list를 추출해서 Service에서 그걸 빼서 productDto로 변환해주는 작업을 해주고 Controller에서 Service 함수 호출해주는 방식으로 진행하면 된다.
    - ListProductRepository.java
    
    ```java
    public List<Product> findAll(){
            return products;
        }
    ```
    
    - SimpleProductService.java
    
    ```java
    public List<ProductDto> findAll(){
            List<Product> products = listProductRepository.findAll();
            List<ProductDto> productDtos = products.stream()
                    .map(product-> modelMapper.map(products, ProductDto.class))
                    .toList();
            return productDtos;
        }
    ```
    
    - 코드 정리
        - Product를 하나하나 ProductDto로 변환해 줘야하는데 이걸 .modelMapper로 하나하나 바꿔주고 list로 변경해주면 된다.
    - ProductController.java
    
    ```java
    @RequestMapping(value ="/products", method = RequestMethod.GET)
        public List<ProductDto> findAllProduct(){
            return simpleProductService.findAll();
        }
    ```
    
3. 상품 이름에 포함된 문자열로 검색하는 기능 추가하기
    - 실행 결과
    
    ![스크린샷 2024-12-29 오후 7.09.43.png](9%E1%84%8C%E1%85%A1%E1%86%BC%20%E1%84%89%E1%85%A1%E1%86%BC%E1%84%91%E1%85%AE%E1%86%B7%20%E1%84%80%E1%85%AA%E1%86%AB%E1%84%85%E1%85%B5%20%E1%84%8B%E1%85%A2%E1%84%91%E1%85%B3%E1%86%AF%E1%84%85%E1%85%B5%E1%84%8F%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%89%E1%85%A7%E1%86%AB%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2016a96aed9b3a80bebcc6efa5f21b691c/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2024-12-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_7.09.43.png)
    
    - 코드 설명
    - 먼저 Product중에 특정 이름이 포함된 제품을 찾기를 원하기 때문에 도메인에 각 인스턴스별로 이름의 값이 포함되어있는지를 확인하는 메소드를 추가해주고 찾을때는 ListProductRepository에서 이름을 가지고 찾아서 List화를 해서 리턴하고 그걸 SimpleProductService에서는 Dto로 바꿔주기만 하면 끝난다. 그리고 위에서 만든 전체 물품 조회랑 합쳐서 Path에 name이라는 파라미터가 주어지면 특정 이름이 포함된 것을 return하고 이름이 주어지지 않으면 전체가 출력되게 만들면된다.
    - Product.java
    
    ```java
    public Boolean containsName(String name){
            return this.name.contains(name);
        }
    ```
    
    - ListProductRepository
    
    ```java
     public List<Product> findByNameContaining(String name){
            return products.stream()
                    .filter(product -> product.containsName(name))
                    .toList();
        }
    ```
    
    - SimpleProductService
    
    ```java
    public List<ProductDto> findByNameContaining(String name){
            List<Product> products = listProductRepository.findByNameContaining(name);
            List<ProductDto> productDtos = products.stream()
                    .map(product-> modelMapper.map(product, ProductDto.class))
                    .toList();
            return productDtos;
        }
    ```
    
    - ProductController
    
    ```java
    @RequestMapping(value ="/products", method = RequestMethod.GET)
        public List<ProductDto> findProducts(@RequestParam(required = false) String name){
            if(null == name){
                return simpleProductService.findAll();
            }
            else{
                return simpleProductService.findByNameContaining(name);
            }
        }
    ```
    
    - getter를 사용하지 않은 이유는 getter를 사용하게 되면 객체지향적인 코드가 아니라 절차지향적인 코드가 된다. 그래서 getter와 setter는 사용을 최대한 지향해야 한다.

1. 상품 수정하기
    - 상품 번호를 기준으로 상품 번호를 제외한 나머지 정보를 수정할 수 있어야 한다.
    - 그렇게 되면 path로 id를 받아야한다.
    - 요청 바디를 통해 상품에 대한 JSON을 받아, 그 값으로 상품을 수정해야 한다.
    - SimpleProductService에서 ProductDto→Product로 변환해주고 ListProductRepository를 호출하고 기존 Product를 id를 통해서 같은 인스턴스를 찾아서 값을 변경해주고 다시 DTO로 변환 후  반환해주면 된다.
    
    - ProductController.java
    
    ```java
    @RequestMapping(value = "/products/{id}", method = RequestMethod.PUT)
        public ProductDto updateProduct(
                @PathVariable Long id,
                @RequestBody ProductDto productDto
        ){
            productDto.setId(id);
            return simpleProductService.update(productDto);
        }
    ```
    
    - setId를 넣은 경우는 만약에 사용자가 body에 id를 넣지 않고 주었다면 저걸로 초기화 하는것이다.
    
    - SimpleProductService.java
    
    ```java
    public ProductDto update(ProductDto productDto){
            Product product = modelMapper.map(productDto, Product.class);
            Product updateProduct = listProductRepository.update(product);
            ProductDto updatedProduct = modelMapper.map(updateProduct, ProductDto.class);
            return updatedProduct;
        }
    ```
    
    - ListProductRepository.java
    
    ```java
     public Product update(Product product){
            Integer indexToModify = products.indexOf(product);
            products.set(indexToModify, product);
            return product;
        }
    ```
    
    - Product.java
    
    ```java
    @Override
        public boolean equals(Object o){
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Product product = (Product)o;
            return Objects.equals(product.id,this.id);
        }
    ```
    
    - equals를 추가한 이유는 인스턴스가 같은지 확인을 하기 위함이고 id값이 같으면 서로 같다고 판단하기위함이다. 위의 ListProductRepository에서 indexOf로 가져올때 사용된다.
    - 현재의 방식대로 Update를 진행하게 되면 하나의 문제 상황이 있다. index를 기준으로 수정할 Product가 선택될때 중간에 수정되거나 삭제되면 다른 값이 가져와 지기 때문이다.
        - 이를 해결하기 위해서는 해당 Product를 remove로 삭제한 후 리스트의 순서와는 무관하게 가장 뒤에 추가한다는 방식이 있다.
        - 또 다른 방법으로는 Product에 Setter를 추가하고, Product조회 후 setter로 값을 수정하는 방법도 있다.

1. 상품 삭제하기
    - 수정과 같이 id를 path로 받아서 SimpleProductService에서 listProductRepositroy에서 id를 통해서 삭제를 한후 그걸 실행시켜주는 역할을 하면 된다.
    - ProductController.java
    
    ```java
    @RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
        public void deleteProduct(@PathVariable Long id){
            simpleProductService.delete(id);
        }
    ```
    
    - SimpleProductService.java
    
    ```java
    public void delete(Long id){
            listProductRepository.delete(id);
        }
    ```
    
    - ListProductRepository.java
    
    ```java
     public void delete(Long id){
            Product product = this.findById(id);
            products.remove(product);
        }
    ```
    
    - 수정과는 다르게 바로 findById로 하는 이유는 remove에서 인스턴스가 서로 같은지 여부를 판단해주기 때문에 id로 따로 찾지 않아도 된다.
    
    - Postman 실습 정리
    
    ![스크린샷 2024-12-29 오후 8.13.45.png](9%E1%84%8C%E1%85%A1%E1%86%BC%20%E1%84%89%E1%85%A1%E1%86%BC%E1%84%91%E1%85%AE%E1%86%B7%20%E1%84%80%E1%85%AA%E1%86%AB%E1%84%85%E1%85%B5%20%E1%84%8B%E1%85%A2%E1%84%91%E1%85%B3%E1%86%AF%E1%84%85%E1%85%B5%E1%84%8F%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%89%E1%85%A7%E1%86%AB%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2016a96aed9b3a80bebcc6efa5f21b691c/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2024-12-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_8.13.45.png)
    
    ![스크린샷 2024-12-29 오후 8.13.37.png](9%E1%84%8C%E1%85%A1%E1%86%BC%20%E1%84%89%E1%85%A1%E1%86%BC%E1%84%91%E1%85%AE%E1%86%B7%20%E1%84%80%E1%85%AA%E1%86%AB%E1%84%85%E1%85%B5%20%E1%84%8B%E1%85%A2%E1%84%91%E1%85%B3%E1%86%AF%E1%84%85%E1%85%B5%E1%84%8F%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%89%E1%85%A7%E1%86%AB%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2016a96aed9b3a80bebcc6efa5f21b691c/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2024-12-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_8.13.37.png)
    
    ![스크린샷 2024-12-29 오후 8.15.00.png](9%E1%84%8C%E1%85%A1%E1%86%BC%20%E1%84%89%E1%85%A1%E1%86%BC%E1%84%91%E1%85%AE%E1%86%B7%20%E1%84%80%E1%85%AA%E1%86%AB%E1%84%85%E1%85%B5%20%E1%84%8B%E1%85%A2%E1%84%91%E1%85%B3%E1%86%AF%E1%84%85%E1%85%B5%E1%84%8F%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%89%E1%85%A7%E1%86%AB%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2016a96aed9b3a80bebcc6efa5f21b691c/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2024-12-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_8.15.00.png)
    
    ![스크린샷 2024-12-29 오후 8.15.04.png](9%E1%84%8C%E1%85%A1%E1%86%BC%20%E1%84%89%E1%85%A1%E1%86%BC%E1%84%91%E1%85%AE%E1%86%B7%20%E1%84%80%E1%85%AA%E1%86%AB%E1%84%85%E1%85%B5%20%E1%84%8B%E1%85%A2%E1%84%91%E1%85%B3%E1%86%AF%E1%84%85%E1%85%B5%E1%84%8F%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%89%E1%85%A7%E1%86%AB%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2016a96aed9b3a80bebcc6efa5f21b691c/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2024-12-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_8.15.04.png)
    
    ![스크린샷 2024-12-29 오후 8.15.27.png](9%E1%84%8C%E1%85%A1%E1%86%BC%20%E1%84%89%E1%85%A1%E1%86%BC%E1%84%91%E1%85%AE%E1%86%B7%20%E1%84%80%E1%85%AA%E1%86%AB%E1%84%85%E1%85%B5%20%E1%84%8B%E1%85%A2%E1%84%91%E1%85%B3%E1%86%AF%E1%84%85%E1%85%B5%E1%84%8F%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%89%E1%85%A7%E1%86%AB%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2016a96aed9b3a80bebcc6efa5f21b691c/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2024-12-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_8.15.27.png)
    
    ![스크린샷 2024-12-29 오후 8.15.34.png](9%E1%84%8C%E1%85%A1%E1%86%BC%20%E1%84%89%E1%85%A1%E1%86%BC%E1%84%91%E1%85%AE%E1%86%B7%20%E1%84%80%E1%85%AA%E1%86%AB%E1%84%85%E1%85%B5%20%E1%84%8B%E1%85%A2%E1%84%91%E1%85%B3%E1%86%AF%E1%84%85%E1%85%B5%E1%84%8F%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%89%E1%85%A7%E1%86%AB%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%2016a96aed9b3a80bebcc6efa5f21b691c/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2024-12-29_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_8.15.34.png)