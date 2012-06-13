package org.scribe.builder.api;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuth20ServiceImpl;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

public class Google2Api extends DefaultApi20
{
  @Override
  public Verb getAccessTokenVerb()
  {
    return Verb.POST;
  }

  @Override
  public AccessTokenExtractor getAccessTokenExtractor()
  {
    return new JsonTokenExtractor();
  }

  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://accounts.google.com/o/oauth2/token";
  }

  private static final String AUTHORIZE_URL = "https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=%s&redirect_uri=%s";
  private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";
  private static final String PARAM_FORMAT = "&%s=%s";

  @Override
  public String getAuthorizationUrl(OAuthConfig config)
  {
    // Append scope if present
    StringBuilder sb = new StringBuilder();
    sb.append(String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback())));

    if (config.hasScope())
      sb.append(String.format(PARAM_FORMAT, OAuthConstants.SCOPE, config.getScope()));
    if (config.hasState())
      sb.append(String.format(PARAM_FORMAT, OAuthConstants.STATE, config.getState()));
    if (config.hasAccessType())
      sb.append(String.format(PARAM_FORMAT, OAuthConstants.ACCESS_TYPE, config.getAccessType()));
    if (config.hasApprovalPrompt())
      sb.append(String.format(PARAM_FORMAT, OAuthConstants.APPROVAL_PROMPT, config.getApprovalPrompt()));

    return sb.toString();
  }

}
