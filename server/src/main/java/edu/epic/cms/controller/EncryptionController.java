package edu.epic.cms.controller;

import edu.epic.cms.api.CommonResponse;
import edu.epic.cms.util.RsaEncryptionUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/encryption")
public class EncryptionController {

    private final RsaEncryptionUtil rsaEncryptionUtil;

    public EncryptionController(RsaEncryptionUtil rsaEncryptionUtil) {
        this.rsaEncryptionUtil = rsaEncryptionUtil;
    }

    @GetMapping("/public-key")
    public ResponseEntity<CommonResponse> getPublicKey() {
        Map<String, String> response = new HashMap<>();
        response.put("publicKey", rsaEncryptionUtil.getPublicKeyBase64());
        return ResponseEntity.ok(CommonResponse.success(response));
    }
}
