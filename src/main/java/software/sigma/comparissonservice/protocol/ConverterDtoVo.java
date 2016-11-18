package software.sigma.comparissonservice.protocol;

import java.util.ArrayList;
import java.util.List;

import software.sigma.comparissonservice.vo.ConfigurationVO;
import software.sigma.comparissonservice.vo.InputDataVO;
import software.sigma.comparissonservice.vo.ResponseVO;
import software.sigma.comparissonservice.vo.SortOrderFieldVO;

/**
 * Convert DTO objects to VO and vice versa.
 * 
 * @author alexandr.efimov
 *
 */
public final class ConverterDtoVo {

	public static ResponseDTO convert(final ResponseVO response) {

		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setErrors(response.getErrors());
		responseDTO.setInformationMessage(response.getInformationMessage());
		responseDTO.setSortedData(response.getSortedData());
		responseDTO.setSuccess(response.isSuccess());
		return responseDTO;
	}

	public static InputDataVO convert(final InputDataDTO inputDataDto) {

		InputDataVO inputDataVO = new InputDataVO();
		inputDataVO.setConfigName(inputDataDto.getConfigName());
		inputDataVO.setDataForSort(inputDataDto.getDataForSort());
		List<SortOrderFieldVO> listFields = new ArrayList<>();
		for (SortOrderFieldDTO fieldDto : inputDataDto.getSortOrder().getFields()) {
			listFields.add(convert(fieldDto));
		}
		inputDataVO.setSortOrder(listFields);
		return inputDataVO;
	}

	public static SortOrderFieldVO convert(SortOrderFieldDTO fieldDto) {
		SortOrderFieldVO sortOrderFieldVO = new SortOrderFieldVO();
		sortOrderFieldVO.setName(fieldDto.getName());
		sortOrderFieldVO.setOrdering(fieldDto.getOrdering());

		return sortOrderFieldVO;
	}

	public static ConfigurationVO convert(ConfigurationDTO configuration) {
		ConfigurationVO configurationVO = new ConfigurationVO();
		configurationVO.setId(configuration.getId());
		configurationVO.setName(configuration.getName());
		configurationVO.setConfigContent(configuration.getConfigContent());
		return configurationVO;
	}

	public static ConfigurationDTO convert(ConfigurationVO configuration) {
		ConfigurationDTO configurationDTO = new ConfigurationDTO();
		configurationDTO.setId(configuration.getId());
		configurationDTO.setName(configuration.getName());
		configurationDTO.setConfigContent(configuration.getConfigContent());
		return configurationDTO;
	}

	public static List<ConfigurationDTO> convert(List<ConfigurationVO> listConfigsVo) {

		List<ConfigurationDTO> list = new ArrayList<>();
		for (ConfigurationVO configurationVO : listConfigsVo) {
			list.add(convert(configurationVO));
		}
		return list;
	}

}
