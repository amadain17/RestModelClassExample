package ie.devine.examples.tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.devine.examples.models.DataModel;
import ie.devine.examples.models.HeadersModel;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import java.io.StringWriter;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static java.lang.Boolean.TRUE;
import static javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.TEXT_XML_VALUE;

public class RestDataXmlTest extends RestDataBase {

    @Test
    public void generateXmlRequestBody() {
        DataModel dataFromModel = DataModel.builder().build();

        Map<String, String> headersFromModel = new ObjectMapper().convertValue(HeadersModel.builder()
                .contentType(TEXT_XML_VALUE)
                .accept(TEXT_XML_VALUE).build(), new TypeReference<Map<String, String>>() {
        });

        String requestBody = getXmlBodyForRequest(dataFromModel);
        Response response = given().spec(requestSpecification).basePath(BASE_PATH)
                .when().headers(headersFromModel).body(requestBody).post()
                .then().statusCode(SC_OK).extract().response();

        String xmlPart = response.getBody().jsonPath().get(RESPONSE_DATA_FIELD);
        XmlPath xmlPath = new XmlPath(xmlPart);
        assertThat(xmlPath.get(XML_BASE + ".@id").toString()).isEqualTo(dataFromModel.getId());
        assertThat(xmlPath.get(XML_BASE + ".position").toString()).isEqualTo(dataFromModel.getPosition());
    }


    @SneakyThrows
    private String getXmlBodyForRequest(DataModel dataFromModel) {
        StringWriter sw = new StringWriter();

        JAXBContext jaxbContext = JAXBContext.newInstance(DataModel.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(JAXB_FORMATTED_OUTPUT, TRUE);

        QName qName = new QName(XML_NS, XML_LOCAL_PART);
        JAXBElement<DataModel> root = new JAXBElement<>(qName, DataModel.class, dataFromModel);

        jaxbMarshaller.marshal(root, sw);
        return sw.toString();
    }
}
