package kr.co.hanbit.product.management.presentation;

import kr.co.hanbit.product.management.application.SimpleProductService;
import kr.co.hanbit.product.management.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {
    SimpleProductService simpleProductService;

    @Autowired
    public ProductController(SimpleProductService simpleProductService) {
        this.simpleProductService = simpleProductService;
    }

    @RequestMapping(value="/products",method = RequestMethod.POST)
    public ProductDto createProduct(@RequestBody ProductDto productdto){
        // Product를 생성하고 리스트에 넣는 작업이 필요함.
        return simpleProductService.add(productdto);
    }

    @RequestMapping(value= "/products/{id}", method = RequestMethod.GET)
    public ProductDto findProductById(@PathVariable Long id)
    {
        return simpleProductService.findById(id);
    }

//    @RequestMapping(value ="/products", method = RequestMethod.GET)
//    public List<ProductDto> findAllProduct(){
//        return simpleProductService.findAll();
//    }

    @RequestMapping(value ="/products", method = RequestMethod.GET)
    public List<ProductDto> findProducts(@RequestParam(required = false) String name){
        if(name == null){
            return simpleProductService.findAll();
        }
        else{
            return simpleProductService.findByNameContaining(name);
        }
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.PUT)
    public ProductDto updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDto productDto
    ){
        productDto.setId(id);
        return simpleProductService.update(productDto);
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
    public void deleteProduct(@PathVariable Long id){
        simpleProductService.delete(id);
    }

}
