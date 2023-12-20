package dev.luizveronesi.autoconfigure.security;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import dev.luizveronesi.autoconfigure.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IUser implements Serializable {

	@JsonAlias("sub")
    private String id;
	private String email;
	private String picture;
	private String name;
	private String token;
	private String status;
	private List<String> roles;
	
	@JsonIgnore
	private transient Map<String, Object> data;
	
	@JsonAnyGetter
	public Map<String, Object> get() {
		return this.data;
	}

	@JsonAnySetter
	public void set(String key, Object value) {
		if (this.data == null) this.data = new HashMap<>();
		this.data.put(key, value);
	}
	
	public String toString() {
		return JsonUtil.serialize(this);
	}
}
