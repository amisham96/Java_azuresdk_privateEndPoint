package com.myappjavaazure;

import java.util.Arrays;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azure.core.credential.TokenCredential;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.EnvironmentCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.network.fluent.models.PrivateEndpointInner;
import com.azure.resourcemanager.network.fluent.models.SubnetInner;
import com.azure.resourcemanager.network.models.PrivateLinkServiceConnection;

@RestController
public class AzureSdkController {
	
	//credentials has been configured as an environment variables
	static AzureProfile profile = new AzureProfile(AzureEnvironment.AZURE);
	static TokenCredential credential = new EnvironmentCredentialBuilder()
			.authorityHost(profile.getEnvironment().getActiveDirectoryEndpoint()).build();
	static AzureResourceManager azure = AzureResourceManager.configure().authenticate(credential, profile)
			.withSubscription("<subscription_id>");

	@GetMapping("/create")
	public void createPrivateEndpoint() {
		azure.networks().manager().serviceClient().getPrivateEndpoints().createOrUpdate("rg1", "mypv1",
				new PrivateEndpointInner().withLocation("eastus").withSubnet(new SubnetInner().withId(
						"/subscriptions/<subscription_id>/resourceGroups/rg1/providers/Microsoft.Network/virtualNetworks/myVnet/subnets/mySubnet"))
						.withPrivateLinkServiceConnections(Arrays.asList(new PrivateLinkServiceConnection()
								.withName("testPls")
								.withPrivateLinkServiceId(
										"/subscriptions/<subscription_id>/resourceGroups/rg1/providers/Microsoft.Network/privateLinkServices/testPls")
								.withGroupIds(Arrays.asList()).withRequestMessage("Please approve my connection"))));
	}

}
